/*

$(document).ready(function() {
    var roomStatuses = {};

    $(".room-calendar").each(function() {
        var roomId = $(this).data("room-id");
        roomStatuses[roomId] = {};
        var roomDates = $(this).data("room-dates").split(",");
        for (var i = 0; i < roomDates.length; i++) {
            var roomDate = roomDates[i];
            roomStatuses[roomId][roomDate] = $(this).data("room-status-" + roomDate);
        }
    });

    $(".room-calendar").datepicker({
        beforeShowDay: function(date) {
            var dateString = $.datepicker.formatDate('yy-mm-dd', date);
            var roomId = $(this).data("room-id");
            var reservedDates = [];

            // API'ye istek yaparak rezervasyon yapılan tarihleri alın
            $.ajax({
                url: "/reservations/reserved-dates/" + roomId,
                method: "GET",
                async: false,
                success: function(data) {
                    reservedDates = data;
                },
                error: function() {
                    console.log("Error while fetching reserved dates.");
                }
            });

            // Alınan tarihleri kontrol ederek renklendirmeyi uygulayın
            if (reservedDates.includes(dateString)) {
                return [true, "lightyellow", "Reserved"];
            }

            return [true];
        }

    });



    $(".room-calendar").css({
        "font-size": "12px",
        "cursor": "pointer"
    }).click(function() {
        $(this).datepicker("show");
    });
});
*/

/*$(".room-calendar").each(function() {
    var roomId = $(this).data("room-id");
    var roomDates = $(this).data("room-dates").split(",");

    $(this).datepicker({
        beforeShowDay: function(date) {
            var dateString = $.datepicker.formatDate("yy-mm-dd", date);

            if (roomDates.includes(dateString)) {
                $(this).datepicker({
                    beforeShowDay: function(date) {
                        var dateString = $.datepicker.formatDate("yy-mm-dd", date);

                        if (roomDates.includes(dateString)) {
                            // Rezervasyon yapılan tarihleri renklendirmek için özel sınıflar ve durum metni kullanabilirsiniz.
                            return [true, "reserved", "Reserved"];
                        }

                        // Rezervasyon yapılmayan tarihler için varsayılan dönüş değeri döndürün.
                        return [true, "", ""];
                    }
                });

                return [true, "custom-class", "Reserved"];
            }

            // Rezervasyon yapılmayan tarihler için varsayılan dönüş değeri döndürün.
            return [true, "", ""];
        }
    });
});*/
/*
$(document).ready(function() {
    // Rezervasyon yapılan tarihleri almak için API'ye istek yapın veya veritabanından çekin
    $.ajax({
        url: 'reservation/get-reserved-dates',
        method: 'GET',
        success: function(reservedDates) {
            // Tüm takvimler için döngü oluşturun
            $('.room-calendar').each(function() {
                var calendar = $(this);

                // Takvim ayarlarını yapılandırın
                calendar.datepicker({
                    beforeShowDay: function(date) {
                        var dateString = $.datepicker.formatDate('yy-mm-dd', date);

                        if (reservedDates.includes(dateString)) {
                            // Rezervasyon yapılan tarihleri devre dışı bırakmak için [false] döndürün
                            return [false];
                        }

                        // Rezervasyon yapılmayan tarihler için [true] döndürün
                        return [true];
                    }
                });
            });
        },
        error: function() {
            console.log('Rezervasyon yapılan tarihleri alırken bir hata oluştu.');
        }
    });
});
*/
/*
$(document).ready(function() {
    // Öğretmen evi seçildiğinde çalışacak fonksiyon
    $("#teacherHome").on("change", function() {
        var teacherHomeId = $(this).val();

        if (teacherHomeId) {
            // Öğretmen evinin odalarını ve rezervasyon tarihlerini almak için API'ye istek yapın
            $.ajax({
                url: "/reservations/rooms-calendar/" + teacherHomeId,
                method: "GET",
                success: function(data) {
                    // Gelen verileri kullanarak takvimleri güncelleyin
                    updateRoomCalendars(data);
                },
                error: function() {
                    console.log("Error while fetching room reservations.");
                }
            });
        } else {
            // Öğretmen evi seçimi yapılmadığında takvimleri temizleyin
            clearRoomCalendars();
        }
    });

    function updateRoomCalendars(data) {
        // Tüm takvimleri sıfırlayın
        $(".room-calendar").datepicker("destroy");

        // Her bir oda takvimi için işlemleri gerçekleştirin
        $(".room-calendar").each(function() {
            var roomId = $(this).data("room-id");
            var roomDates = data[roomId];

            // Takvim ayarlarını yapılandırın
            $(this).datepicker({
                beforeShowDay: function(date) {
                    var dateString = $.datepicker.formatDate("yy-mm-dd", date);

                    if (roomDates && roomDates.includes(dateString)) {
                        // Rezervasyon yapılan tarihleri devre dışı bırakmak için [false] döndürün
                        return [false, "reserved", "Reserved"];
                    }

                    // Rezervasyon yapılmayan tarihler için [true] döndürün
                    return [true];
                }
            });
        });
    }

    function clearRoomCalendars() {
        // Tüm takvimleri sıfırlayın
        $(".room-calendar").datepicker("destroy");
    }
});*/
/*
$(document).ready(function() {
    // Öğretmenevinin odalarının rezervasyonlu tarihlerini almak için API'ye istek yapın veya veritabanından çekin
    $.ajax({
        url: '/reservations/get-reserved-dates-by-teacherhome',
        method: 'GET',
        data: { teacherHomeId: selectedTeacherHomeId }, // Seçilen öğretmenevinin kimliğini geçirin
        success: function(reservedDates) {
            // Tüm takvimler için döngü oluşturun
            $('.room-calendar').each(function() {
                var calendar = $(this);
                var roomId = calendar.data('room-id');

                // Takvim ayarlarını yapılandırın
                calendar.datepicker({
                    beforeShowDay: function(date) {
                        var dateString = $.datepicker.formatDate('yy-mm-dd', date);

                        if (reservedDates[roomId].includes(dateString)) {
                            // Rezervasyonlu tarihleri devre dışı bırakmak için [false] döndürün
                            return [false];
                        }

                        // Rezervasyon yapılmayan tarihler için [true] döndürün
                        return [true];
                    }
                });
            });
        },
        error: function() {
            console.log('Rezervasyonlu tarihleri alırken bir hata oluştu.');
        }
    });
});
*/
/*$(document).ready(function() {
    var reservedDates = {};

    $(".room-calendar").each(function() {
        var roomId = $(this).data("room-id");

        // API'ye istek yaparak rezervasyon yapılan tarihleri alın
        $.ajax({
            url: `/rooms/${roomId}/reservations`,
            method: "GET",
            async: false,
            beforeSend: function() {
                console.log(`Sending request to: /rooms/${roomId}/reservations`);
            },
            success: function(data) {
                reservedDates = data;
            },
            error: function() {
                console.log("Error while fetching reserved dates.");
            }
        });


    });

    $(".room-calendar").datepicker({
        beforeShowDay: function(date) {
            var dateString = $.datepicker.formatDate('yy-mm-dd', date);
            var roomId = $(this).data("room-id");

            // Alınan tarihleri kontrol ederek renklendirmeyi uygulayın
            if (reservedDates[roomId] && reservedDates[roomId].includes(dateString)) {
                return [false, "lightyellow", "Reserved"];
            }

            return [true];
        }
    }).on("click", function() {
        $(this).datepicker("show");
    });
});*/
$(document).ready(function() {
    $(".room-calendar").each(function() {
        var roomId = $(this).data("room-id");
        var statusDatesString = $(this).data("room-dates");

        if (!statusDatesString) {
            console.error(`Room with ID ${roomId} does not have status dates.`);
            return;  // Bu odayı atla ve döngünün sonraki öğesine geç
        }
        var statusDatesArray = statusDatesString.split(",");
        var allDates = [];
        var statusOfDates = {};  // yeni bir nesne oluşturduk, her tarih için durum bilgisini saklayacağız

        statusDatesArray.forEach(function(statusDate) {
            var range = statusDate.split("=")[0];
            var status = statusDate.split("=")[1]; // durum bilgisini al
            var startDateString = range.split("-")[0] + "-" + range.split("-")[1] + "-" + range.split("-")[2];
            var endDateString = range.split("-")[3] + "-" + range.split("-")[4] + "-" + range.split("-")[5];

            var startDate = new Date(startDateString);
            var endDate = new Date(endDateString);

            for (var d = new Date(startDate); d <= endDate; d.setDate(d.getDate() + 1)) {
                var formattedDate = $.datepicker.formatDate('yy-mm-dd', new Date(d));
                allDates.push(formattedDate);
                statusOfDates[formattedDate] = status;  // her tarih için durum bilgisini sakla
            }
        });

        $(this).datepicker({
            beforeShowDay: function(date) {
                var dateString = $.datepicker.formatDate('yy-mm-dd', date);

                if (allDates.includes(dateString)) {
                    // durum bilgisine göre renk belirle
                    var status = statusOfDates[dateString];
                    var color;
                    var tooltip;

                    switch (status) {
                        case "CANCELLED":
                            color = "red";
                            tooltip = "Cancelled";
                            break;
                        case "CONFIRMED":
                            color = "lightgreen";
                            tooltip = "Confirmed";
                            break;
                        case "PENDING":
                            color = "grey";
                            tooltip = "Pending";
                            break;
                        default:
                            color = "red";
                            tooltip = "Unavailable";
                            break;
                    }

                    return [false, color, tooltip];
                }

                return [true];
            }
        }).on("click", function() {
            $(this).datepicker("show");
        });
    });
});

/*Bu kod, statusDates'in değerini HTML elementinden alır ve bunu kullanarak tarih aralığındaki tüm tarihleri allDates adlı bir diziye ekler. Bu dizi, takvimde hangi tarihlerin etkin olup olmadığını kontrol etmek için kullanılabilir.

Bu kodun çalışabilmesi için sayfanızda jQuery'nin ve jQuery UI'nin yü*/



