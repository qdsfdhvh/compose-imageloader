// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Compose ImageLoader',
  tagline: 'Compose Image library for Kotlin Multiplatform',
  favicon: 'img/favicon.ico',

  url: 'https://qdsfdhvh.github.io',
  baseUrl: '/compose-imageloader',
  organizationName: 'seiko',
  projectName: 'compose-imageloader',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  trailingSlash: false,

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          editUrl: 'https://github.com/qdsfdhvh/compose-imageloader/tree/main/docs/',
        },
        blog: {
          showReadingTime: true,
          editUrl: 'https://github.com/qdsfdhvh/compose-imageloader/tree/main/docs/',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: 'img/opg.png',
      navbar: {
        title: 'Compose ImageLoader',
        items: [
          {
            to: 'docs/setup',
            label: 'Setup',
            position: 'left',
          },
          {
            to: 'docs/core/basic',
            label: 'Docs',
            position: 'left',
          },
          {
            href: '/api/index.html',
            label: 'API',
            position: 'left',
            target: '_blank',
          },
          {
            href: 'https://github.com/qdsfdhvh/compose-imageloader',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
      },
    }),
};

module.exports = config;
