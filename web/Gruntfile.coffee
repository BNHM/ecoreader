#spawn = require('child_process').spawn
#require("load-grunt-tasks")(grunt)

module.exports = (grunt) ->
  # Gruntfile
  # https://github.com/sindresorhus/grunt-shell
  grunt.loadNpmTasks("grunt-shell")
  # https://www.npmjs.com/package/grunt-contrib-coffee
  grunt.loadNpmTasks("grunt-contrib-coffee")
  # https://github.com/gruntjs/grunt-contrib-watch
  grunt.loadNpmTasks("grunt-contrib-watch")
  grunt.loadNpmTasks("grunt-contrib-uglify")
  grunt.loadNpmTasks("grunt-contrib-cssmin")
  # Validators
  grunt.loadNpmTasks('grunt-bootlint')
  grunt.loadNpmTasks('grunt-html')
  grunt.loadNpmTasks('grunt-string-replace')
  grunt.loadNpmTasks('grunt-postcss')
  grunt.loadNpmTasks('grunt-contrib-less')
  grunt.initConfig
    pkg: grunt.file.readJSON('package.json')
    shell:
      options:
        stderr: false
      bower:
        command: ["bower update"].join("&&")
      npm:
        command: ["npm install", "npm update"].join("&&")
      movesrc:
        command: ["ls"] # ["mv js/c.src.coffee js/maps/c.src.coffee"].join("&&")
    postcss:
      options:
        processors: [
          require('autoprefixer')({browsers: 'last 1 version'})
          ]
      dist:
        src: "css/main.css"
      drop:
        src: "css/shadow-dropzone.css"
    uglify:
      options:
        mangle:
          except:['jQuery']
      vulcanize:
        options:
          sourceMap:true
          sourceMapName:"js/maps/app.js.map"
        files:
          "js/app.min.js":["app-prerelease.js"]
      combine:
        options:
          sourceMap:true
          sourceMapName:"js/maps/combined.map"
          sourceMapIncludeSources:true
          sourceMapIn:"js/maps/c.js.map"
        files:
          "js/combined.min.js":["js/c.js","js/admin.js","bower_components/purl/purl.js","bower_components/xmlToJSON/lib/xmlToJSON.js","bower_components/jquery-cookie/jquery.cookie.js"]
          "js/app.min.js":["js/c.js","js/admin.js"]
      dist:
        options:
          sourceMap:true
          sourceMapName:"js/maps/c.map"
          sourceMapIncludeSources:true
          sourceMapIn:"js/maps/c.js.map"
          compress:
            # From https://github.com/mishoo/UglifyJS2#compressor-options
            dead_code: true
            unsafe: true
            conditionals: true
            unused: true
            loops: true
            if_return: true
            drop_console: false
            warnings: true
            properties: true
            sequences: true
            cascade: true
        files:
          "js/c.min.js":["js/c.js"]
      minpurl:
        options:
          sourceMap:true
          sourceMapName:"js/maps/purl.map"
        files:
          "js/purl.min.js": ["bower_components/purl/purl.js"]
      minxmljson:
        options:
          sourceMap:true
          sourceMapName:"js/maps/xmlToJSON.map"
        files:
          "js/xmlToJSON.min.js": ["bower_components/xmlToJSON/lib/xmlToJSON.js"]
      minjcookie:
        options:
          sourceMap:true
          sourceMapName:"js/maps/jquery.cookie.map"
        files:
          "js/jquery.cookie.min.js": ["bower_components/jquery-cookie/jquery.cookie.js"]
    less:
      # https://github.com/gruntjs/grunt-contrib-less
      options:
        sourceMap: true
        outputSourceFiles: true
        banner: "/*** Compiled from LESS source ***/\n\n"
      files:
        dest: "css/main.css"
        src: ["less/main.less"]
    cssmin:
      options:
        sourceMap: true
        advanced: false
      target:
        files:
          "css/main.min.css":["css/main.css"]
          "css/dropzone.min.css":["css/shadow-dropzone.css"]
    coffee:
      compile:
        options:
          bare: true
          join: true
          sourceMapDir: "js/maps"
          sourceMap: true
        files:
          "js/c.js":["coffee/*.coffee"]
    watch:
      scripts:
        files: ["coffee/*.coffee"]
        tasks: ["coffee:compile","uglify:dist"] #,"shell:movesrc"]
      styles:
        files: ["less/main.less"]
        tasks: ["less","postcss","cssmin"]
  ## Now the tasks
  grunt.registerTask("default",["watch"])
  grunt.registerTask("compile","Compile coffeescript",["coffee:compile","uglify:dist"]) #,"shell:movesrc"])
  ## The minification tasks
  # Part 1
  grunt.registerTask("minifyIndependent","Minify Bower components that aren't distributed min'd",["uglify:minpurl","uglify:minxmljson","uglify:minjcookie"])
  # Part 2
  grunt.registerTask("minifyBulk","Minify the major things",["uglify:combine","uglify:dist"])
  grunt.registerTask "css", "Process LESS -> CSS", ["less","postcss","cssmin"]
  # Main call
  grunt.registerTask "minify","Minify all the things",->
    grunt.task.run("minifyIndependent","minifyBulk","css")
  ## Global update
  # Bower
  grunt.registerTask("updateBower","Update bower dependencies",["shell:bower"])
  grunt.registerTask("updateNPM","Update Node dependencies",["shell:npm"])
  # Minify the bower stuff in case it changed
  grunt.registerTask "update","Update dependencies", ->
    grunt.task.run("updateNPM","updateBower","minify")
  ## Deploy
  grunt.registerTask "qbuild","Compile then watch", ->
    grunt.task.run("compile","minify","watch")

  grunt.registerTask "build","Compile and update, then watch", ->
    # ,"vulcanize"
    grunt.task.run("updateNPM","updateBower","compile","minify","watch")
