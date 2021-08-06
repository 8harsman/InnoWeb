//sets up the two filtering range sliders on "Home"-page (index.html) using jQuery-UI

 //sets up range-slider for "Anzahl Bewertungen"
 $( function() {

    $( "#slider-range" ).slider({
      start: function (event, ui) {
             $("#slider-range").slider().find(".ui-slider-handle").css("background","#e2011f");        },
      stop: function( event, ui ) {$("#slider-range").slider().find(".ui-slider-handle").css("background","#f6f6f6");},
      range: true,
      min: 0,
      max: 3,
      values: [ 0, 3 ],
      slide: function( event, ui ) {
        $( "#amount" ).val( + ui.values[ 0 ] + " - " + ui.values[ 1 ] );

      }
    });
    $( "#amount" ).val($( "#slider-range" ).slider( "values", 0 ) +
      " - " + $( "#slider-range" ).slider( "values", 1 ) );
  } );


 //sets up range-slider for "Potenzial"
   $( function() {

    $( "#slider-range2" ).slider({
      start: function (event, ui) {
             $("#slider-range2").slider().find(".ui-slider-handle").css("background","#e2011f");        },
      stop: function( event, ui ) {$("#slider-range2").slider().find(".ui-slider-handle").css("background","#f6f6f6");},
      range: true,
      min: 0,
      max: 40,
      values: [ 0, 40 ],
      slide: function( event, ui ) {
        $( "#amount2" ).val( + ui.values[ 0 ] + " - " + ui.values[ 1 ] );
      }
    });
    $( "#amount2" ).val($( "#slider-range2" ).slider( "values", 0 ) +
      " - " + $( "#slider-range2" ).slider( "values", 1 ) );
  } );