//Inhabits the js-logic for filtering innovations in the table on "Home"-page (index.html)

$(document).ready(function() {

// Bootstrap datepicker
$('.input-daterange input').each(function() {
  $(this).datepicker('clearDates'); //initialise Date
});

$('.date-range-filter').datepicker(); //initialise bootstrap-datepicker-framework on the html-element

// Extend dataTables search
// Extend DataTable-Filter with Date-Inputs from HTML
$.fn.dataTable.ext.search.push(
  function(settings, data, dataIndex) {
    var min = $('#minDatum').val();
    var max = $('#maxDatum').val();

    var createdAt = data[5] || 5; // column with date value in our table
    if ( //cases in which the entry should be shown in the table
      (min == "" && moment(createdAt, "DD.MM.YYYY").isSameOrBefore(moment(max, "MM-DD-YYYY" )) ) ||  (max == "" && moment(createdAt, "DD.MM.YYYY").isSameOrAfter(moment(min, "MM-DD-YYYY" )) ) || (max == "" && min == "") ||
      (moment(createdAt, "DD.MM.YYYY").isSameOrAfter(moment(min,"MM-DD-YYYY" )) && moment(createdAt, "DD.MM.YYYY").isSameOrBefore(moment(max, "MM-DD-YYYY" )))
    ) {
      return true; // return true to show entry in the table
    }
    return false; // return true to not show entry in the table
  }
);



/* Custom filtering function which will search data in column 3 between two values */
// is used for our Anzahl-Bewertungen-Filter
$.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        var min = parseInt( $('#slider-range').slider("values")[0], 10 ); //gets the min value from the range-slider
        var max = parseInt( $('#slider-range').slider("values")[1], 10 ); //gets the max value from the range-slider
        var anzahl = parseFloat( data[3] ) || 0; // use data for the fitting column

        if ( ( isNaN( min ) && isNaN( max ) ) ||
             ( isNaN( min ) && anzahl <= max ) ||
             ( min <= anzahl   && isNaN( max ) ) ||
             ( min <= anzahl   && anzahl <= max ) )
        {
            return true;  // return true to show entry in the table
        }
        return false; // return true to not show entry in the table
    }
);


/* Custom filtering function which will search data in column four between two values */
// is used for our Potenzial-Filter
$.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        var min = parseInt( $('#slider-range2').slider("values")[0], 10 );//gets the min value from the range-slider
        var max = parseInt( $('#slider-range2').slider("values")[1], 10 );//gets the max value from the range-slider
        var anzahl = parseFloat( data[2] ) || 0; // use data for the fitting column

        if ( ( isNaN( min ) && isNaN( max ) ) ||
             ( isNaN( min ) && anzahl <= max ) ||
             ( min <= anzahl   && isNaN( max ) ) ||
             ( min <= anzahl   && anzahl <= max ) )
        {
            return true; // return true to show entry in the table
        }
        return false; // return true to not show entry in the table
    }
);

/* Custom filtering function which will search data in column four between two values */
// is used for our Titel/Beschreibung/Ersteller-Filter
$.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        var wert =  $('#SuchBox').val().toLowerCase();
        console.log("Wert" + wert);
        var datum = ( data[4].toLowerCase() ) || 0; // use data for the fitting column (Ersteller)
        var datum1 = ( data[0].toLowerCase() ) || 0; // use data for the fitting column (Titel)
        var datum2 = ( data[1].toLowerCase() ) || ""; // use data for the fitting column (Beschreibung)

        if ( wert==""  || ( datum.includes(wert)) || ( datum1.includes(wert)) || ( datum2.includes(wert)) )
        {
            return true; // return true to show entry in the table
        }
        return false; // return true to not show entry in the table
    }
);



    // DataTable
    //Initialisation of the HTML-Table as DataTable
    var table = $('#myTable').DataTable({

        "dom": 'lrtip', //change layout of DataTable
        initComplete: function () {

             // Event listener to the date inputs to redraw on input
             $('.date-range-filter').on( 'change', function () {

                            table.search()
                            table.draw();
                } );


             //Suchbox (Titel, Beschreibung, Ersteller)
             // Event listener to the text-input
                 $('#SuchBox').keyup( function() {
                  table.search()
                 table.draw();
                   } );

              //Anzahl Bewertungen
              // Event listener to the range filtering inputs to redraw on input
              $( "#slider-range" ).on( "slidechange", function( event, ui )
              {
                 table.search()
                 table.draw();} );

              //Potenzial
              // Event listener to the range filtering inputs to redraw on input
              $( "#slider-range2" ).on( "slidechange", function( event, ui )
              {
                 table.search()
                 table.draw();} );


        }

    });

});