const express = require('express');
const router = express.Router();
const moment = require('moment');
const { db, admin } = require('../config/db')
moment.suppressDeprecationWarnings = true;

router.get('/api/reserve', (req, res) => {
    // in the reserve page, we have hours at the left which show all the hours and if an hour is busy
    // we have hours between 6 am to 9 pm.
    // so you have 15 hours after your current hour, some of them in the same day, and the rest are in the 
    global.prevHours = [];
    global.nextHours = [];
    var time;
    var allTime = [];
    var currentTimeHour = moment(Date.now()).utcOffset(180).hour();
    var count;
    var timeIncluded;
    if (req.query.zoneName == 'CBAE') {
        req.query.zoneName = 'CBAE Female & Male Zone'
    } else if (req.query.zoneName == 'LIB') {
        req.query.zoneName = 'LIB Female & Male Zone'
    }
    // get reservations which has status of created
    db.ref('reservations').orderByChild('status').equalTo('created').once('value').then(snapshot => {
        snapshot.forEach(reservation => {
            // loop through all the reservations 
            // loop through reservation 
            if (reservation.val()) {
                if (reservation.val().time) {
                    var currentTime = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
                    var resTime = moment(`${reservation.val().date} ${reservation.val().time.pop()}:00`).format('YYYY-MM-DD HH:mm')
                    var diff = moment(resTime).diff(currentTime, 'seconds');
                    if (req.query.zoneName) {
                        if (reservation.val().zoneName == req.query.zoneName) {
                            time = reservation.val().time;
                            console.log(time);
                            console.log('diff', diff);
                            // loop through reservation time and push every time in the allTime array
                            if (diff > 0) {
                                for (i = 0; i < time.length; i++) {
                                    allTime.push(Number(time[i]));
                                }
                            }
                        }
                    }
                }
            }
        })
        // loop through hours between current hour and 9 pm
        if (currentTimeHour < 5) {
            currentTimeHour = 5;
        }
        for (var i = currentTimeHour; i <= 21; i++) {
            if (allTime.length > 0) {
                // check if allTime hours includes i ( which increases from current hour to 21 in the loop )
                timeIncluded = allTime.includes(i);
                if (timeIncluded == true) {
                    // if i included do the following.
                    // create a counter with value 0 to check if an hour is included 4 times, so it will is blocked
                    count = 0;
                    for (var j = 0; j < allTime.length; j++) {
                        // loop through allTime hours if it’s equal to (i) increment the counter
                        if (allTime[j] == i) {
                            count++
                        }
                    }
                    // if the counter equals 4 then this hour is busy.
                    if (count >= 4) {
                        prevHours.push({
                            hour: i,
                            status: 'busy'
                        });
                    } else if (count == 3) {
                        prevHours.push({
                            hour: i,
                            status: '75'
                        });
                    } else if (count == 2) {
                        prevHours.push({
                            hour: i,
                            status: '50'
                        });
                    } else if (count == 1) {
                        prevHours.push({
                            hour: i,
                            status: '25'
                        });
                    } else {
                        prevHours.push({
                            hour: i,
                            status: 'free'
                        });
                    }
                } else {
                    prevHours.push({
                        hour: i,
                        status: 'free'
                    });
                }
            } else {
                prevHours.push({
                    hour: i,
                    status: 'free'
                });
            }
        }
        prevHours.shift();
        // loop through all hours between 5 am and current hour
        for (var i = 5; i < currentTimeHour; i++) {
            if (allTime.length > 0) {
                // check if allTime hours includes i ( which increases from 5 to current hour in the loop )
                timeIncluded = allTime.includes(i);
                if (timeIncluded == true) {
                    // if i included do the following.
                    // create a counter with value 0 to check if an hour is included 4 times, so it will is blocked
                    count = 0;
                    for (var j = 0; j < allTime.length; j++) {
                        // loop through allTime hours if it’s equal to (i) increment the counter
                        if (allTime[j] == i) {
                            count++
                        }
                    }
                    // if the counter equals 4 then this hour is busy.
                    if (count >= 4) {
                        nextHours.push({
                            hour: i,
                            status: 'busy'
                        });
                    } else if (count == 3) {
                        nextHours.push({
                            hour: i,
                            status: '75'
                        });
                    } else if (count == 2) {
                        nextHours.push({
                            hour: i,
                            status: '50'
                        });
                    } else if (count == 1) {
                        nextHours.push({
                            hour: i,
                            status: '25'
                        });
                    } else {
                        nextHours.push({
                            hour: i,
                            status: 'free'
                        });
                    }
                } else {
                    nextHours.push({
                        hour: i,
                        status: 'free'
                    });
                }
            } else {
                nextHours.push({
                    hour: i,
                    status: 'free'
                });
            }
        }
        nextHours.shift();
        res.json({
            "nextHours": nextHours,
            "prevHours": prevHours
        });
    }).catch(err => {
        console.log(err)
    })
})

router.post('/register', (req, res) => {
    // check if the request contains all the data we need
    var uid = `${req.body.uid}`;
    for (var i = 0; i < uid.length; i++) {
        uid = uid.replace(' ', '');
        uid = uid.replace(/[^a-zA-Z0-9]/g, '');
    }
    var errors = [];
    if (req.body.email && req.body.password && req.body.first_name && req.body.last_name && req.body.carnumber && req.body.mobile && req.body.uid) {
        if (req.body.first_name.length < 3 || req.body.last_name.length < 3) {
            errors.push(`Name can't be less than 3 characters`)
        }
        if (req.body.carnumber.length < 3 || req.body.carnumber.length > 6) {
            errors.push(`Car number isn't a valid number`)
        }
        if (req.body.mobile.length != 8) {
            errors.push(`Mobile number isn't a valid number`)
        }
        if (req.body.password.length < 6 || req.body.password.length > 12) {
            errors.push(`Password can't be less than 6 or more than 12`)
        }
        if (uid.length != 8) {
            errors.push(`Uid can't be more than 8 characters`)
        }

        if (errors.length > 0) {
            res.render('register', {
                errors: errors
            })
        } else {
            // create user authentication record
            admin.auth().createUser({
                email: req.body.email,
                password: req.body.password,
            })
                .then(function (userRecord) {
                    if (userRecord) {
                        var result = '';
                        for (var i = 0; i < uid.length; i++) {
                            if (i % 2 == 0 && i != 0) {
                                result += ' ';
                            }
                            result += uid.charAt(i);
                        }
                        result = result.toUpperCase();
                        // create the user in the db
                        var newUser = db.ref('/users').child(`${userRecord.uid}`);
                        newUser.set({
                            email: req.body.email,
                            name: req.body.first_name + ' ' + req.body.last_name,
                            plateNo: req.body.carnumber,
                            mobile: req.body.mobile,
                            uid: result
                        }).then(() => {
                            res.send(`User registered, You can now login!`)
                        })
                    } else {
                        res.send(`Error creating new user`)
                    }
                })
                .catch(function (error) {
                    res.send(`Error creating new user: ${error}`)
                });
        }
    }
})

router.post('/api/reserve', (req, res, next) => {
    // check if the request has time, date, zone and hours
    if (req.body.time && req.body.date && req.body.zones && req.body.hours) {
        // split the time and get just the number of the hour
        var time = req.body.time.substring(0, req.body.time.indexOf(':'));
        // get just the hours between 6 am and 10 pm
        if (Number(time) > 22 || Number(time) <= 6) {
            res.send("Reservation time is from 6 am to 10 pm");
        } else {
            if (req.body.userID) {
                db.ref('/users').child(`${req.body.userID}`).once('value')
                    .then((user) => {
                        // get the user data by his id
                        if (user) {
                            if (req.body.zones == 'CBAE') {
                                req.body.zones = 'CBAE Female & Male Zone'
                            } else if (req.body.zones == 'LIB') {
                                req.body.zones = 'LIB Female & Male Zone'
                            }
                            var plateNo = user.val().plateNo;
                            var uid = user.val().uid;
                            var hours = Number(req.body.hours);
                            var reservationDate = moment(`${req.body.date} ${req.body.time}`).format('YYYY-MM-DD HH:mm');
                            var currentDate = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
                            // get the difference between the reservation time and current time in minutes
                            var difference = moment(reservationDate).diff(currentDate, 'minutes');
                            // make the difference in hours
                            difference = difference / 60;
                            if (difference > 24) {
                                res.send("can't reserve before more than 24 hrs");
                                return;
                            } else if (difference <= 0) {
                                res.send("This time is already passed");
                                return;
                            } else {
                                var ref = db.ref('/reservations');
                                // get all reservations
                                global.resNo = 1;
                                ref.once('value').then(snapshot => {
                                    var userResTime = 0;
                                    var count = 0;
                                    var timeCount = [];
                                    var currentTimeCount = [];
                                    var isReserved = false;
                                    global.reservationsTime;
                                    reservationsTime = [];
                                    // loop through all reservations
                                    snapshot.forEach((reservation) => {
                                        var reservation = reservation.val();
                                        resNo = Number(reservation.resNo) + 1;
                                        var resTime;
                                        if (reservation.time) {
                                            resTime = Number(reservation.time.length);
                                            for (var i = 0; i < reservation.time.length; i++) {
                                                timeCount.push(reservation.time[i]);
                                                // loop through all the reservations time to check if this time and zone is reserved 4 times, so it’s blocked
                                                if (reservation.date === req.body.date) {
                                                    if (reservation.status === "created") {
                                                        if (reservation.zoneName === req.body.zones) {
                                                            // push all the times reserved before - in the same zone, date and status 'created' - into an array
                                                            reservationsTime.push(Number(reservation.time[i]));
                                                        }
                                                    }
                                                }
                                            }
                                            // here we are counting the number of hours for the user to detect if they are more than 6 hours
                                            if (reservation.carPlateNo === plateNo && reservation.status === 'created') {
                                                userResTime = userResTime + resTime;
                                            }
                                        }
                                    });
                                    // loop through the reservation hours
                                    for (var i = 0; i < hours; i++) {
                                        var currentTime = req.body.time.substring(0, req.body.time.indexOf(':'));
                                        // increment hours to the current hour
                                        currentTime = Number(currentTime) + i;
                                        currentTimeCount.push(currentTime);
                                    }
                                    // get reservations by the which has the car plate number of this user   
                                    global.timeIncluded = false;
                                    global.resZone;
                                    var countTime = 0;
                                    db.ref('/reservations').orderByChild('carPlateNo').equalTo(`${plateNo}`).once('value').then(snapshot => {
                                        snapshot.forEach((reservation) => {
                                            if (reservation) {
                                                var reservation = reservation.val();
                                                // the number of the new reservation will be incrementation of the last reservation 

                                                if (reservation.time) {
                                                    var resTime = reservation.time;
                                                    if (resTime.length > 0) {
                                                        // loop through the user reservations time to check if this user has reserved in this time before
                                                        for (var i = 0; i < resTime.length; i++) {
                                                            for (var j = 0; j < currentTimeCount; j++) {
                                                                if (reservation.zoneName === req.body.zones) {
                                                                    if (reservation.date === req.body.date) {
                                                                        if (reservation.status == 'created') {
                                                                            if (Number(resTime[i]) === currentTimeCount[j]) {
                                                                                // if he has reserved then (isReserved) will be true
                                                                                isReserved = true;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                        // check if (isReserved) is true then display error message to the user and redirect him to /reserve page
                                        if (isReserved) {
                                            res.send("You can’t reserve, as you have already reserved at this time and zone");
                                        } else {
                                            // check if the user all time hours is 6 so he can’t reserve more
                                            if (Number(userResTime) > 6) {
                                                res.send("Sorry! You can’t reserve more as your reservations exceed 6 hours");
                                                return next();
                                            } else {
                                                // check if the user all time hours and the reservation hours is 6 so he can’t reserve more
                                                if (Number(userResTime) + hours > 6) {
                                                    res.send("Sorry! You can’t reserve more as your reservations exceed 6 hours");
                                                    return next();
                                                } else {
                                                    // check if all time reservations array - which we have pushed into in the last step - has data in it, which means the database is not empty of reservations.
                                                    // so we don't get error if it's empty
                                                    if (reservationsTime.length > 0) {
                                                        // loop through the time of this reservation to check if the all reservations time array includes the new reservation times
                                                        for (i = 0; i < currentTimeCount.length; i++) {
                                                            timeIncluded = reservationsTime.includes(currentTimeCount[i]);
                                                            // if the array includes an hour of the new reservation, then we will set timeIncluded to true.
                                                            // then loop through the all time reservations again to check how many times does it include this hour
                                                            if (timeIncluded == true) {
                                                                for (var j = 0; j < reservationsTime.length; j++) {
                                                                    if (reservationsTime[j] == currentTimeCount[i]) {
                                                                        countTime++
                                                                    }
                                                                }
                                                                // if this hour in the reservation hours is included 4 times in the reservations time, then he can't reserve at this time
                                                                if (countTime >= 4) {
                                                                    res.send(`This time ${currentTimeCount[i]} is already reserved, please try another time or another zone`);
                                                                    return;
                                                                } else {
                                                                    next();
                                                                }
                                                            } else {
                                                                next();
                                                            }
                                                        }
                                                    } else {
                                                        next();
                                                    }
                                                    // after checking and if everything is ok, we will proceed to adding the reservation
                                                    // the price will be number of hours multiplied by 5
                                                    // sending success message that he has reserved succesfully and redirect the user to the home page
                                                    res.send("successfully reserved");
                                                }
                                            }
                                        }
                                    }).catch(err => {
                                        console.log(err);
                                    })
                                }).catch(err => {
                                    console.log(err)
                                })
                            }
                        } else {
                            res.send('No User')
                            return;
                        }
                    })
            } else {
                res.send('No User')
                return;
            }
        }
    } else {
        res.send("error_msg", "Please enter all the fields");
        return;
    }
})

router.post('/extend', (req, res, next) => {
    // get exact reservation by id
    if (req.body.reserveID) {
        db.ref(`/reservations/${req.body.reserveID}`).once('value').then(snapshot => {
            if (snapshot.val() == null) {
                res.send("No reservation with this id");
                return;
            }
            var extendedHours = 0;
            // check if this reservation has extended hours before, so we will increment it. And if not it will be 0
            if (snapshot.val().extendedHours) {
                extendedHours = Number(snapshot.val().extendedHours);
            }
            var price = snapshot.val().price;
            var time = snapshot.val().time;
            var resZone = snapshot.val().zoneName;
            var date = snapshot.val().date;
            var carPlateNo = snapshot.val().carPlateNo;
            var resDate;
            // get the last item in the array ( last hour in the reservation ) to get the difference between it and the current time hours
            var lastResHour = time.pop();
            // extended hour will be incrementation of the last hour of reservation
            var extTime = Number(lastResHour) + 1;
            var time2;
            var resTime;
            // handle the time format so that it can be used to get the difference
            resTime = moment(`${snapshot.val().date} ${extTime}:00`, 'YYYY-MM-DD H:mm').format('YYYY-MM-DD HH:mm');
            resDate = snapshot.val().date;
            var currentTime = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
            var difference = moment(resTime).diff(currentTime, 'minutes');
            global.allTime = [];
            global.timeIncluded = false;
            global.userIsReserved = false;
            global.userReservedTime = [];
            var countTime = 0;
            // get reservations which are created with the extended reservation’s plate number
            db.ref('/reservations').orderByChild('carPlateNo').equalTo(`${snapshot.val().carPlateNo}`).once('value').then(snapshot => {
                snapshot.forEach(reservation => {
                    if (reservation) {
                        var reservedTime = reservation.val().time;
                        if (reservedTime) {
                            // loop through user reservations times to check if this user has 6 hours
                            for (var i = 0; i < reservedTime.length; i++) {
                                if (reservation.val().status == 'created') {
                                    var timeNow = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
                                    var reservationTime = moment(`${reservation.val().date} ${reservation.val().time.pop()}:00`, 'YYYY-MM-DD H:mm').format('YYYY-MM-DD HH:mm')
                                    var diff = moment(reservationTime).diff(timeNow, 'seconds');
                                    if (diff > 0) {
                                        userReservedTime.push(reservedTime[i]);
                                    }
                                }
                            }
                        }
                    }
                })
                if (userReservedTime.includes(extTime)) {
                    userIsReserved = true
                }
                if (userReservedTime.length >= 6) {
                    res.send("you can't reserve more than 6 hours")
                    return;
                } else if (userIsReserved == true) {
                    res.send("you can't extend at this time. you have already reserved")
                    return;
                } else {
                    // get reservations that has the same extended reservation date
                    db.ref('/reservations').orderByChild('date').equalTo(`${date}`).once('value').then(snapshot => {
                        snapshot.forEach(reservation => {
                            if (reservation) {
                                var time = reservation.val().time;
                                if (reservation.val().status == 'created') {
                                    var timeNow = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
                                    var reservationTime = moment(`${reservation.val().date} ${reservation.val().time.pop()}:00`, 'YYYY-MM-DD H:mm').format('YYYY-MM-DD HH:mm')
                                    var diff = moment(reservationTime).diff(timeNow, 'seconds');
                                    if (diff > 0) {
                                        console.log(time);
                                        // push every reservation hour that has the same date and status created to allTime array.
                                        for (i = 0; i < time.length; i++) {
                                            allTime.push(time[i]);
                                        }
                                        // then check if allTime array includes the extended hour
                                        timeIncluded = allTime.includes(extTime);
                                    }
                                }
                            }
                        });
                        // if the array includes the extended hour, then we will loop through the array again to check how many times does it have the extended hour.
                        if (timeIncluded == true) {
                            for (var i = 0; i < allTime.length; i++) {
                                if (allTime[i] == `${extTime}`) {
                                    countTime++;
                                }
                            }
                            console.log(countTime);
                            // if the array includes the extended hour 4 times, then it will display error that this time is already reserved
                            if (countTime >= 4) {
                                res.send("Sorry, You can't extend. There's no available parking spots!")
                                return;
                            }
                        }
                        // check if the difference between the reservation last hour and the extended hour is between 0 and 59 mins
                        if (difference <= 59 && difference > 0) {
                            var endTimeExt;
                            time.push(lastResHour);
                            // if the extended reservation is just one hour, then we will get the extend time using this way, and then increment it
                            if (time.length == 1) {
                                endTimeExt = Number(time[0]) + 1;
                                // if the extended reservation is more than one hour, then we will get the extend time using this way, and then increment it
                            } else if (time.length > 1) {
                                var endTime = Number(time.pop());
                                time.push(`${endTime}`);
                                endTimeExt = endTime + 1;
                            }
                            // the new extended hours will be number of the exist extended hours incremented by 1
                            extendedHours = extendedHours + 1;
                            // and the price will be the previous price incremented by 5 which is price of one hour
                            price = price + 5;
                            // updating the reservation with the new details

                            var timeRef = db.ref(`/reservations/${req.body.reserveID}/time`);
                            timeRef.child(time.length).set(endTimeExt).then((reservation) => {
                                db.ref(`/reservations/${req.body.reserveID}`).update({
                                    extendedHours: extendedHours,
                                    price: price
                                }).then(() => {
                                    res.send("Reservation extended")
                                })
                            }).catch(err => {
                                console.log(err);
                            });
                        } else if (difference < 0) {
                            // show error if the difference is less than 0, which means that the reservation is already finished
                            res.send('This reservation is already finished');
                        } else {
                            // show error if the difference is more than 59 mins that he can't extend before more than one hour
                            res.send('You can\'t extend before more than 1 hr');
                        }
                    });
                }
            })
        }).catch(err => {
            console.log(err)
        })
    } else {
        res.send('Error');
    }
})

router.post('/cancel', (req, res) => {
    // get the reservation that we need to cancel using its id
    if (req.body.reserveID) {
        db.ref(`/reservations/${req.body.reserveID}`).once('value').then(reservation => {
            var time = reservation.val().time;
            var timeLen = time.length;
            var currentRes = -time.length;
            // take the first hour from the time hours array
            var firstHour = time.shift();
            time.unshift(firstHour);
            var time2;
            // get the difference between current time and the reservation time in days, so we can move like the logic
            var currentTime = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
            var resTime = moment(`${reservation.val().date} 0${firstHour}:00`).format('YYYY-MM-DD HH:mm');
            var day = moment(resTime).diff(currentTime, 'day');
            // check if the reservation is in another day
            if (day > 0) {
                // update the reservation, make it cancel and delete the hours. and update the cancelled hours
                db.ref(`/reservations/${req.body.reserveID}`).update({
                    status: 'cancelled',
                    time: [],
                    price: 0,
                    cancelledHours: time.length
                }).then(() => {
                    res.send('Reservation was cancelled Successfully!');
                })
            } else if (day == 0) {
                // if the reservation is in the same day
                var penPrice, nonPenPrice, price, cancelledHours = 0;
                resTime = moment(`${reservation.val().date} ${firstHour}:00`).format('YYYY-MM-DD HH:mm');
                currentTime = moment(Date.now()).utcOffset(180).format('YYYY-MM-DD HH:mm');
                // get the difference between them in minutes
                var difference = moment(resTime).diff(currentTime, 'minutes');
                var timeNow = moment(Date.now()).utcOffset(180).hour();
                // then get the difference in hours, to check if the reservation has started or not
                difference = difference / 60;
                var timePen = [];
                var nonTimePen = [];
                var status = '';
                if (difference < 0 && difference > currentRes) {
                    // if the reservation started, loop through the time hours, and push the hours which didn't work to (timePen)
                    // which will have penalty, and push the hours are equal or less than the current hour to (nonTimePen)
                    for (var i = 0; i < time.length; i++) {
                        if (Number(time[i]) > timeNow) {
                            timePen.push(time[i]);
                        } else if (Number(time[i]) < timeNow || Number(time[i]) == timeNow) {
                            nonTimePen.push(time[i]);
                        }
                    }
                    // price of hours which have penalty, will be multiplied by 5 and divided by two
                    penPrice = Number(timePen.length) * 5;
                    penPrice = penPrice / 2;
                    // price of hours which don't have penalty will be multiplied by 5
                    nonPenPrice = Number(nonTimePen.length) * 5;
                    // cancelled hours will be the penalty time hours
                    cancelledHours = timePen.length;
                    // the whole price will be equal the non penalty and the penalty hours price combined
                    price = nonPenPrice + penPrice;
                    status = 'subcancelled';
                } else if (difference < currentRes) {
                    // if the difference between the two times is more than the number of hours reserved, this means that this reservation has ended
                    // and will show an error message
                    res.send('This reservation has ended. You can\'t cancel it')
                    return;
                } else if (difference > 0) {
                    // if the reservation hasn't started, then the price will be the hours multiplied by 5 then divided by two
                    price = reservation.val().price;
                    price = Number(price) / 2;
                    cancelledHours = timeLen;
                    status = 'cancelled'
                }
                // update the reservation with the new details
                db.ref(`/reservations/${req.body.reserveID}`).update({
                    status: status,
                    price: price,
                    cancelledHours: cancelledHours
                }).then(() => {
                    res.send('Reservation was cancelled Successfully!')
                })
            }
        }).catch(err => {
            console.log(err);
        })
    } else {
        res.send('error')
    }
})

router.get('/statistics', (req, res, done) => {
    db.ref('/zones').once('value').then(snapshot => {
        snapshot.forEach(zone => {
            var days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
            var hisRef = db.ref(`/zones/${zone.key}/statistics`);
            for (i = 0; i < 7; i++) {
                hisRef.child(i).set({
                    day: days[i],
                    hoursInfo: []
                })
                for (j = 0; j < 17; j++) {
                    db.ref(`/zones/${zone.key}/statistics/${i}/hoursInfo`).child(j).set({
                        count: 0,
                        hour: `${j + 6}`
                    });
                }
            }
        })
        db.ref('/reservations').remove();
        res.send('Bravo');
        done();
    })
});

module.exports = router;