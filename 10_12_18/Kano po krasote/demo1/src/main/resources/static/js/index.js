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
            alert(arrTSD);
            /*var arrForChart = JSON.parse(arrTSD);*///return из SpringBoot
        },
        failure: function (errMsg) {
            alert(errMsg);
        }
    })
}

document.addEventListener('DOMContentLoaded', init);//Ждём пока страница загрузится, затем вызываем функцию init