/**
 * @license
 * Copyright (C) 2015 Vaadin Ltd.
 * This program is available under Commercial Vaadin Add-On License 3.0 (CVALv3).
 * See the file LICENSE.md distributed with this software for more information about licensing.
 * See [the website]{@link https://vaadin.com/license/cval-3} for the complete license.
 */

import { html, PolymerElement } from '@polymer/polymer/polymer-element';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin';
import { ElementMixin } from '@vaadin/vaadin-element-mixin';
import '@vaadin/vaadin-button';

/**
 * `<vcf-paginator>` Web Component for displaying a full functioning paginator on a webpage.
 *
 * ```html
 * <vcf-paginator></vcf-paginator>
 * ```
 *
 * @memberof Vaadin
 * @mixes ElementMixin
 * @mixes ThemableMixin
 * @demo demo/index.html
 */
class VcfPaginator extends ElementMixin(ThemableMixin(PolymerElement)) {
  static get template() {
    return html`
      <style>
        :host {
          display: block;
        }

        [part='navigation'] {
          display: flex;
          justify-content: space-between;
        }

        [part='page-navigators'] {
          display: flex;
          flex-wrap: wrap;
          justify-content: center;
        }

        [part='first-navigator'] {
          margin-right: 10px;
        }

        [part='last-navigator'] {
          margin-left: 10px;
        }
      </style>

      <div part="navigation">
        <div part="first-navigator">
          <vaadin-button id="firstPageNavigator" theme="small tertiary" on-click="_goToFirstPage">
            [[firstLabel]]
          </vaadin-button>
        </div>
        <div part="page-navigators">
          <template is="dom-repeat" items="[[_pageTitles]]" as="pageLabel">
            <vaadin-button
              selected$="[[_isPageSelected(pageLabel, currentPage)]]"
              theme="small tertiary"
              on-click="_setPage"
              disabled$="[[_isDotPage(pageLabel)]]"
            >
              [[pageLabel]]
            </vaadin-button>
          </template>
        </div>
        <div part="last-navigator">
          <vaadin-button id="lastPageNavigator" theme="small tertiary" on-click="_goToLastPage">
            [[lastLabel]]
          </vaadin-button>
        </div>
      </div>
    `;
  }

  static get is() {
    return 'vcf-paginator';
  }

  static get version() {
    return '1.1.1';
  }

  static get properties() {
    return {
      currentPage: {
        type: Number,
        value: 1,
        observer: '_pageChanged'
      },

      totalPages: {
        type: Number,
        value: 1,
        observer: '_generatePages'
      },

      truncation: {
        type: Boolean,
        value: false,
        reflectToAttribute: true,
        observer: '_generatePages'
      },

      _pageTitles: {
        type: Array,
        value: () => []
      },

      firstLabel: {
        type: String,
        value: 'First'
      },

      lastLabel: {
        type: String,
        value: 'Last'
      }
    };
  }

  _setPage(e) {
    this.setPage(e.model.pageLabel);
  }

  _goToLastPage() {
    this.setPage(this.totalPages);
  }

  _goToFirstPage() {
    this.setPage(1);
  }

  setPage(val) {
    this.set('currentPage', val);
  }

  _pageChanged() {
    if (this.currentPage === 1) {
      this.$.firstPageNavigator.setAttribute('disabled', true);
    } else if (this.$.firstPageNavigator.hasAttribute('disabled')) {
      this.$.firstPageNavigator.removeAttribute('disabled');
    }
    if (this.currentPage === this.totalPages) {
      this.$.lastPageNavigator.setAttribute('disabled', true);
    } else if (this.$.lastPageNavigator.hasAttribute('disabled')) {
      this.$.lastPageNavigator.removeAttribute('disabled');
    }
    if (this.truncation) {
      this._generatePages();
    }
    this.dispatchEvent(
      new CustomEvent('vcf-paginator-page-change', {
        detail: {
          page: this.currentPage
        }
      })
    );
  }

  _generatePages() {
    this.set(
      '_pageTitles',
      this.truncation ? this._truncatedPages(this.currentPage, this.totalPages) : this._allPages(this.totalPages)
    );
  }

  _isCurrentPage(val) {
    return parseInt(val) === this.currentPage;
  }

  _isTheLastPage() {
    return this.currentPage === this.totalPages;
  }

  _isTheFirstPage() {
    return this.currentPage === 1;
  }

  _isPageSelected(pageLabel, currentPage) {
    return pageLabel === currentPage;
  }

  _allPages(totalPages) {
    const pageTitles = [];

    for (let i = 1; i <= totalPages; i++) {
      pageTitles.push(i);
    }
    return pageTitles;
  }

  _truncatedPages(currentPage, lastPage) {
    const delta = 2;
    const left = currentPage - delta;
    const right = currentPage + delta + 1;
    const range = [];
    const rangeWithDots = [];
    let l;

    for (let i = 1; i <= lastPage; i++) {
      if (i == 1 || i == lastPage || (i >= left && i < right)) {
        range.push(i);
      }
    }

    for (const i of range) {
      if (l) {
        if (i - l === 2) {
          rangeWithDots.push(l + 1);
        } else if (i - l !== 1) {
          rangeWithDots.push('...');
        }
      }
      rangeWithDots.push(i);
      l = i;
    }

    return rangeWithDots;
  }

  _isDotPage(index) {
    return index == '...';
  }
}

customElements.define(VcfPaginator.is, VcfPaginator);

/**
 * @namespace Vaadin
 */
window.Vaadin.VcfPaginator = VcfPaginator;
