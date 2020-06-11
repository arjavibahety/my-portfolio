/*!
 * Start Bootstrap - Resume v6.0.0 (https://startbootstrap.com/template-overviews/resume)
 * Copyright 2013-2020 Start Bootstrap
 * Licensed under MIT (https://github.com/BlackrockDigital/startbootstrap-resume/blob/master/LICENSE)
 */
(function ($) {
  'use strict'; // Start of use strict

  // Smooth scrolling using jQuery easing
  $('a.js-scroll-trigger[href*="#"]:not([href="#"])').click(function () {
    if (
      location.pathname.replace(/^\//, '') ==
        this.pathname.replace(/^\//, '') &&
      location.hostname == this.hostname
    ) {
      var target = $(this.hash);
      target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
      if (target.length) {
        $('html, body').animate(
          {
            scrollTop: target.offset().top,
          },
          1000,
          'easeInOutExpo'
        );
        return false;
      }
    }
  });

  // Closes responsive menu when a scroll trigger link is clicked
  $('.js-scroll-trigger').click(function () {
    $('.navbar-collapse').collapse('hide');
  });

  // MDB Lightbox Init
  $(function () {
    $('#mdb-lightbox-ui').load('mdb-addons/mdb-lightbox-ui.html');
  });

  // Activate scrollspy to add active class to navbar items on scroll
  $('body').scrollspy({
    target: '#sideNav',
  });
})(jQuery); // End of use strict

async function getData() {
    const response = await fetch('/data');
    const responseJson = await response.json();
    console.log("Response: ", response);
    
    const responseLstElem = document.getElementById('greeting-container');
    responseLstElem.innerHTML = '';
    for (let key of Object.keys(responseJson)) {
        console.log(key + " -> " + responseJson[key]);
        responseLstElem.appendChild(
        createParaElement(responseJson[key]));
    }
}

/** Creates an <li> element containing text. */
function createParaElement(text) {
  const paraElement = document.createElement('p');
  paraElement.innerText = text;
  return paraElement;
}
