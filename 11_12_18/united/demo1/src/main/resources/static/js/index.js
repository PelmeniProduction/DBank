var UPDATE_BUTTON = 'update_data';//id кнопки Обновить данные
var SELECT_CLASS = '.form-control';//класс выпадающего окна
var CHECK_CLASS = '.custom-control-input';//класс чекбокса

function init() {
    //находим кнопки по id
    var Update = document.getElementById(UPDATE_BUTTON);
    //обрабатываем клики на них
    Update.addEventListener("click", function (e) {
        getData();
        //document.location.href = 'http://localhost:8080/update';
    });
}

function getData() {
    var Select = document.querySelector(SELECT_CLASS);//присваиваем переменной выпадающее окно
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
            console.log(arrTSD);
            amountOfCharts(arrTSD);
        },
        failure: function (errMsg) {
            alert(errMsg);
        }
    });
}

function amountOfCharts(returnArray){
    var Select2 = document.querySelector(SELECT_CLASS);//присваиваем переменной выпадающее окно
    //Меняем заголовок в зависимости от выбранной категории
    if(Select2.options[Select2.selectedIndex].text === "Золото")
    {
        $("#graphTitle").text("Курс золота (руб./гр.)");
    }
    else if(Select2.options[Select2.selectedIndex].text === "Нефть")
    {
        $("#graphTitle").text("Курс нефти ($/барр. IPE)");
    }
    //обновляем график полученными данными
    myChart.data.labels = returnArray.map(tsd => tsd.date);
    //проверяем какие чекбоксы отмечены
    var CheckBox = document.querySelectorAll(CHECK_CLASS);
    var arr = [0,0,0];
    var sArr = "";
    var ii;
    for(ii=0; ii<CheckBox.length; ii++)
    {
        if(CheckBox[ii].type === "checkbox" && CheckBox[ii].checked)
        {
            arr[ii] = 1;
        }
        sArr+=arr[ii];
    }
    //выводим графики соответственно выбранным чекбоксам
    switch (sArr) {
        case "000"://нет доп графиков
            myChart.data.datasets = [datasetOne];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            break;
        case "100":
            myChart.data.datasets = [datasetOne, datasetTwo];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dKalman);
            break;
        case "110":
            myChart.data.datasets = [datasetOne, datasetTwo, datasetThree];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dKalman);
            myChart.data.datasets[2].data = returnArray.map(tsd => tsd.dEMA);
            break;
        case "111"://все доп графики
            myChart.data.datasets = [datasetOne, datasetTwo, datasetThree, datasetFour];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dKalman);
            myChart.data.datasets[2].data = returnArray.map(tsd => tsd.dEMA);
            myChart.data.datasets[3].data = returnArray.map(tsd => tsd.dSMA);
            break;
        case "001":
            myChart.data.datasets = [datasetOne, datasetFour];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dSMA);
            break;
        case "011":
            myChart.data.datasets = [datasetOne, datasetThree, datasetFour];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dEMA);
            myChart.data.datasets[2].data = returnArray.map(tsd => tsd.dSMA);
            break;
        case "010":
            myChart.data.datasets = [datasetOne, datasetThree];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dEMA);
            break;
        case "101":
            myChart.data.datasets = [datasetOne, datasetTwo, datasetFour];
            myChart.data.datasets[0].data = returnArray.map(tsd => tsd.dCoast);
            myChart.data.datasets[1].data = returnArray.map(tsd => tsd.dKalman);
            myChart.data.datasets[2].data = returnArray.map(tsd => tsd.dSMA);
            break;
    }
    myChart.update();
}

document.addEventListener('DOMContentLoaded', init);//Ждём пока страница загрузится, затем вызываем функцию init