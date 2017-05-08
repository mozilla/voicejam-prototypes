(function(window) {

  var positionStr = '25.0020295,121.5310567';
  var position = positionStr.split(',');

  var map = null;

  function onFile() {
    var fileEl = document.querySelector('.fileToUpload');
    var file = fileEl.files[0];
    var previewEl = preview(file);
    navigator.geolocation.getCurrentPosition(function(position) {
      console.log(position)
      var posStr = position.coords.latitude + ', ' + position.coords.longitude;
      var htmlStr = '<div class="position">' + posStr + '</div>';
      previewEl.appendChild(toElement(htmlStr));
    });
  }

  function showGoogleMap(position) {
    var latLng = new google.maps.LatLng(position[0], position[1]);
    var mapOptions = {
        zoom: 16, // initialize zoom level - the max value is 21
        streetViewControl: false, // hide the yellow Street View pegman
        scaleControl: true, // allow users to zoom the Google Map
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: latLng
    };

    map = new google.maps.Map(document.querySelector('#googlemap'), mapOptions);
    console.log(map)

    // Show the default red marker at the location
    marker = new google.maps.Marker({
      position: latLng,
      map: map,
      draggable: false,
      animation: google.maps.Animation.DROP
    });
  }

  function onReady() {
    // Init map
    showGoogleMap(position);

    // Update location in background map
    navigator.geolocation.getCurrentPosition(function(pos) {
      if (pos.coords.latitude != position[0] || pos.coords.longitude != position[1]) {
        position = [pos.coords.latitude, pos.coords.longitude];
        map.setCenter({ lat: position[0], lng: position[1] });
      }
    });

    // Listen for new recordings
    document.querySelector('.fileToUpload').addEventListener('change', onFile);
  }
  document.addEventListener('DOMContentLoaded', onReady);

  function log(msg) {
    console.log(msg);
    //var htmlStr = '<div>' + msg + '</div>';
    //document.body.appendChild(new DOMParser().parseFromString(htmlStr, 'text/html').body.firstChild);
  }

  function preview(file) {
    var previewContainer = document.createElement('div');
    previewContainer.classList.add('preview');
    document.querySelector('.list').appendChild(previewContainer);
    var el = null;
    if (file.type.indexOf('video') == 0) {
      el = document.createElement('video');
    }
    else {
      el = document.createElement('img');
    }

    el.classList.add('obj');
    el.file = file;
    previewContainer.appendChild(el);
    var reader = new FileReader();
    reader.onload = (function(aEl) {
      return function(e) {
        aEl.src = e.target.result;
        if (aEl.tagName == 'VIDEO') {
          // TODO: figure out to show preview image
          el.play();
          el.pause();
        }
      };
    })(el);
    reader.readAsDataURL(file);
    return previewContainer;
  }

  function toElement(str) {
    return new DOMParser().parseFromString(str, 'text/html').body.firstChild;
  }

})(window);
