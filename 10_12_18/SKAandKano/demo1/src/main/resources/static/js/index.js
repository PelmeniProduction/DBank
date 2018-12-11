var UPDATE_BUTTON = 'update_data';//id кнопки
var SELECT_CLASS = '.form-control';//класс выпадающего окна

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
            //обновляем график полученными данными
            myChart.data.labels = arrTSD.map(tsd => tsd.date);
            myChart.data.datasets = [datasetOne, datasetTwo, datasetThree];
            /*myChart.data.datasets.forEach((dataset) => {
                dataset.data = arrTSD.map(tsd => tsd.price);
            });*///так сказал сделать Михаил
            myChart.data.datasets[0].data = arrTSD.map(tsd => tsd.price);
            myChart.data.datasets[1].data = arrTSD.map(tsd => tsd.price/2);
            myChart.data.datasets[2].data = arrTSD.map(tsd => tsd.price*2);//для трёх графиков

            myChart.update();
        },
        failure: function (errMsg) {
            alert(errMsg);
        }
    })
}

document.addEventListener('DOMContentLoaded', init);//Ждём пока страница загрузится, затем вызываем функцию init