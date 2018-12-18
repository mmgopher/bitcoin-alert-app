var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/bitcoin-alert-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/bitcoin/alerts', function (alert) {
            showAlerts(JSON.parse(alert.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function addAction() {
$.ajax({
    url: '/alert',
    type: 'PUT',
    data: {
        pair:$('#pair').val(),
        limit:$('#limit').val()
    },
     dataType: "html"
})}


function deleteAction(method) {
$.ajax({
    url: '/alert?'+$.param({"pair": $('#pair').val(), "limit" : $('#limit').val()}),
    type: 'DELETE',
     dataType: "html"
})}

function showAlerts(alert) {
    $("#alerts").append("<tr><td>" + alert.currencyPair + "</td><td>" + alert.limit +
     "</td><td>" + alert.current +
     "</td><td>" + alert.timestamp + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#addAlert" ).click(function() { addAction(); });
    $( "#deleteAlert" ).click(function() { deleteAction(); });
});

