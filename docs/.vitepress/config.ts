import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'HoloEasy',
  description: 'A simple Minecraft hologram library for Java and Kotlin',
  base: '/holoeasy/',
  cleanUrls: true,
  lastUpdated: true,

  themeConfig: {
    logo: undefined,
    nav: [
      { text: 'Guide', link: '/guide/what-is-holoeasy' },
      { text: 'GitHub', link: 'https://github.com/denmeh/holoeasy' }
    ],

    sidebar: [
      {
        text: 'Getting started',
        items: [
          { text: 'What is HoloEasy?', link: '/guide/what-is-holoeasy' },
          { text: 'Requirements', link: '/guide/requirements' },
          { text: 'Installation', link: '/guide/installation' },
          { text: 'Your first hologram', link: '/guide/first-hologram' }
        ]
      },
      {
        text: 'Core concepts',
        items: [
          { text: 'How holograms work', link: '/guide/how-it-works' },
          { text: 'Lines', link: '/guide/lines' },
          { text: 'Pools', link: '/guide/pools' },
          { text: 'Click interactions', link: '/guide/interactions' }
        ]
      },
      {
        text: 'Examples',
        items: [
          { text: 'Example plugins', link: '/guide/examples' }
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/denmeh/holoeasy' }
    ],

    search: {
      provider: 'local'
    },

    editLink: {
      pattern: 'https://github.com/denmeh/holoeasy/edit/master/docs/:path',
      text: 'Edit this page on GitHub'
    },

    footer: {
      message: 'Released under the GNU GPL v3 License.',
      copyright: 'Copyright © HoloEasy contributors'
    }
  }
})
