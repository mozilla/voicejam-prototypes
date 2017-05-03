var fileEl = document.querySelector('.fileToUpload');


function onFile() {
	var fileEl = document.querySelector('.fileToUpload');
	var file = fileEl.files[0];
  log(file.type);
  var previewEl = preview(file);
	navigator.geolocation.getCurrentPosition(function(position) {
  	console.log(position.coords.latitude, position.coords.longitude);
	});
}

fileEl.addEventListener('change', onFile);

function log(msg) {
  console.log(msg);
  //var htmlStr = '<div>' + msg + '</div>';
  //document.body.appendChild(new DOMParser().parseFromString(htmlStr, 'text/html').body.firstChild);
}

function uploadFile() {
	var fd = new FormData();
	var count = document.getElementById('fileToUpload').files.length;
	for (var index = 0; index < count; index ++) {
		var file = document.getElementById('fileToUpload').files[index];
		fd.append(file.name, file);
	}
	var xhr = new XMLHttpRequest();
	xhr.upload.addEventListener("progress", uploadProgress, false);
	xhr.addEventListener("load", uploadComplete, false);
	xhr.addEventListener("error", uploadFailed, false);
	xhr.addEventListener("abort", uploadCanceled, false);
	xhr.open("POST", "savetofile.aspx");
	xhr.send(fd);
}

function preview(file) {
  var el = null;
  if (file.type.indexOf('video') == 0) {
	  el = document.createElement('video');
  }
  else {
	  el = document.createElement('img');
  }

  el.classList.add('obj', 'preview');
  el.file = file;
  document.querySelector('.list').appendChild(el);
  var reader = new FileReader();
  reader.onload = (function(aEl) {
		return function(e) {
      aEl.src = e.target.result;
      if (aEl.tagName == 'VIDEO') {
        // Hack to show preview
        aEl.play();
        aEl.pause();
      }
    };
  })(el);
  reader.readAsDataURL(file);
  return el;
}
