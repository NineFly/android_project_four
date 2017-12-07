cordova.define("com-sarriaroman-photoviewer.PhotoViewer", function(require, exports, module) {
var exec = require('cordova/exec');

exports.show = function(url, title, options) {
    if( title == undefined ) {
      title = '';
    }
    if (imgIndex == undefined){
        imgIndex = 0;
    }
    if(typeof options == "undefined"){
        options = {};
    }

    exec(function(){}, function(){}, "PhotoViewer", "show", [url, title, options]);
};

});
