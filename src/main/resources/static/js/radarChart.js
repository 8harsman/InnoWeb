
/*
Creates a radar Chart based on how many ratings there are (@param anzahl), the actual ratings
(@param bew), a given title (@aram title) and a possible label (@param dataLabel) for each rating
This function depends on a canvas element in a html file to work successfully.

*/
function createRadarChart(anzahl, bew, title, dataLabel = null)
{
    if (bew !== null)
    {
        var bColor = ['rgba(226, 1, 31, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(0, 128, 0, 0.2)'];
        var dataArr = []
        for (var i = 0; i < Math.min(3, anzahl); i++) {
            var data = bew[i].ratings
            // usefull when comparing average ratings of ideas
            if (bew[i].ersteller !== null)
            {
                var label = bew[i].ersteller.vorname + " " + bew[i].ersteller.nachname
            }
            else
            {
                var label = dataLabel[i]
            }
            var rating_at_i =
            {
                label: label,
                data: data,
                backgroundColor: bColor[i],
                fill: true,
                spanGaps: true,
            }
            dataArr.push(rating_at_i)

        }
        var data =
        {
            labels: ["Kriterium1", "Kriterium2", "Kriterium3", "Kriterium4",
                    "Kriterium5", "Kriterium6", "Kriterium7","Kriterium8"],
            datasets: dataArr,
        };
        var chartOptions =
        {
        responsive: true,
        maintainAspectRatio: false,
            plugins:
            {
                title:
                {
                    display: true,
                    text: title,
                }
            },
            elements:
            {
                line:
                {
                    borderWidth: 2,
                    borderColor: 'rgba(0, 0, 0, 0.6)',
                }
            },
            scale:
            {
                r:
                {
                    beginAtZero: true,
                    suggestedMin: 0,
                    suggestedMax: 5,
                    ticks:
                    {
                        stepSize: 1,
                    }
                }
            }
        };
       var ctxR = document.getElementById("radarChart").getContext('2d');
       var radarChart = new Chart(ctxR,
        {
            type: 'radar',
            data: data,
            options: chartOptions
        });

    }

}