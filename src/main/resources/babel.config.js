module.exports = {
    presets: [
      [
        '@babel/preset-env',
        {
          useBuiltIns: 'usage'
        }
      ],
      '@babel/preset-react',
      'linaria/babel',
      "@linaria"    
    ],
    plugins: [
      '@babel/plugin-syntax-dynamic-import',
      'react-hot-loader/babel'
    ]
  }