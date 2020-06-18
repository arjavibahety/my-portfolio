// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/** Creates a map and adds it to the page. */
function createMap() {
  const india = { name: 'India', lat: 25.3407, lng: 74.6313 };
  const singapore = { name: 'Singapore', lat: 1.3521, lng: 103.8198 };
  const indonesia = { name: 'Indonesia', lat: 6.2088, lng: 106.8456 };
  const malaysia = { name: 'Malaysia', lat: 4.2105, lng: 101.9758 };
  const uk = { name: 'UK', lat: 55.3781, lng: 3.4360 };
  const locations = [india, singapore, indonesia, malaysia, uk];

  const darkModeStyle = [
    { elementType: 'geometry', stylers: [{ color: '#242f3e' }]},
    { elementType: 'labels.text.stroke', stylers: [{ color: '#242f3e' }]},
    { elementType: 'labels.text.fill', stylers: [{ color: '#edba68' }]},
    {
      featureType: 'administrative.locality',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#d59563' }]
    },
    {
      featureType: 'poi',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#d59563' }]
    },
    {
      featureType: 'poi.park',
      elementType: 'geometry',
      stylers: [{ color: '#263c3f' }]
    },
    {
      featureType: 'poi.park',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#6b9a76' }]
    },
    {
      featureType: 'road',
      elementType: 'geometry',
      stylers: [{ color: '#38414e' }]
    },
    {
      featureType: 'road',
      elementType: 'geometry.stroke',
      stylers: [{ color: '#212a37' }]
    },
    {
      featureType: 'road',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#9ca5b3' }]
    },
    {
      featureType: 'road.highway',
      elementType: 'geometry',
      stylers: [{ color: '#edba68' }]
    },
    {
      featureType: 'road.highway',
      elementType: 'geometry.stroke',
      stylers: [{ color: '#1f2835' }]
    },
    {
      featureType: 'road.highway',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#f3d19c' }]
    },
    {
      featureType: 'transit',
      elementType: 'geometry',
      stylers: [{ color: '#2f3948' }]
    },
    {
      featureType: 'transit.station',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#d59563' }]
    },
    {
      featureType: 'water',
      elementType: 'geometry',
      stylers: [{ color: '#17263c' }]
    },
    {
      featureType: 'water',
      elementType: 'labels.text.fill',
      stylers: [{ color: '#515c6d' }]
    },
    {
      featureType: 'water',
      elementType: 'labels.text.stroke',
      stylers: [{ color: '#17263c' }]
    }
  ];

  const map = new google.maps.Map(
    document.getElementById('map-container'), {
      center: india, 
      zoom: 3,
      styles: darkModeStyle
    }
  );

  const bounds = new google.maps.LatLngBounds();
  const infowindow = new google.maps.InfoWindow();   

  for (i = 0; i < locations.length; i++) {  
    const marker = new google.maps.Marker({
      position: new google.maps.LatLng(locations[i]['lat'], locations[i]['lng']),
      map: map
    });

    // extend the bounds to include each marker's position
    bounds.extend(marker.position);

    google.maps.event.addListener(marker, 'click', (function(marker, i) {
      return function() {
        infowindow.setContent(locations[i]['name']);
        infowindow.open(map, marker);
      }
    })(marker, i));
  }

  // fit the map to the newly inclusive bounds
  map.fitBounds(bounds);

  // restore the zoom level after the map is done scaling
  const listener = google.maps.event.addListener(map, "idle", function () {
    map.setZoom(3);
    google.maps.event.removeListener(listener);
  });
}

/** Adds GMaps script tag to the page. */
function loadGoogleMapsScript(){
  const script = document.createElement("script")
  script.type = "text/javascript";

  if (script.readyState){  //IE
    script.onreadystatechange = function(){
      if (script.readyState == "loaded" ||
              script.readyState == "complete"){
          script.onreadystatechange = null;
      }
    };
  } else {  //Others
    script.onload = function(){
    };
  }

  script.src = config.GMAPS_URL + config.GMAPS_API_KEY;
  document.getElementsByTagName("head")[0].appendChild(script);
}
