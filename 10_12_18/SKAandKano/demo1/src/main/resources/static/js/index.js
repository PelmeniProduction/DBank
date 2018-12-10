var UPDATE_BUTTON = 'update_data';//id кнопки
var SELECT_CLASS = '.form-control';//класс выпадающего окна
var ctx = document.getElementById("myChart");
var myChart;


function init() {
    var Update = document.getElementById(UPDATE_BUTTON);//находим кнопку по id
    Update.addEventListener("click", function (e) {//обрабатываем клик на неё
        getData();
        //document.location.href = 'http://localhost:8080/update';
    });
}
function getData() {
    console.log('function called');
    var Select = document.querySelector(SELECT_CLASS);
    console.log(Select.options[Select.selectedIndex].text);//выводим выбранный пункт выпадающего меню select
    console.log($('#DRP1').val());//выводим выбранный промежуток времени
    //ajax-запрос-post
    $.ajax({
        type: "POST",
        url: "/update",
        data: JSON.stringify({"category": Select.options[Select.selectedIndex].text, "period": $('#DRP1').val()}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (arrTSD) {
            /*var arrForChart = arrTSD;//return из SpringBoot
            console.log(arrTSD);
            console.log(arrTSD[0].date);
            console.log(arrTSD[1].category);
            console.log(arrTSD[2].price);*/
            /*var i;
            var sLabels = "";
            var sData = "";
            for(i=0; i<arrTSD.length; i++)
            {
                sLabels += arrTSD[i].date + ", ";
                sData += arrTSD[i].price + ", ";
            }
            console.log(sLabels);
            console.log(sData);*/
            myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: arrTSD.map(function(tsd) {
                        return tsd.date;
                    }),
                    datasets: [{
                        data: arrTSD.map(function(tsd) {
                            return tsd.price;
                        }),
                        lineTension: 0,
                        backgroundColor: 'transparent',
                        borderColor: '#007bff',
                        borderWidth: 4,
                        pointBackgroundColor: '#007bff'
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: false
                            }
                        }]
                    },
                    legend: {
                        display: false,
                    }
                }
            });
        },
        failure: function (errMsg) {
            alert(errMsg);
        }
    })
}

document.addEventListener('DOMContentLoaded', init);//Ждём пока страница загрузится, затем вызываем функцию init