/**
 * Created by lenovo on 2015/7/24.
 */
var startDayMS = $("#startday").val() || '',
// timelist = $("#timelist").val()||'',
servicetimeList = {}, isUpdating = false, typelocal = 'caboliinfo';
var current_time = '';
if (timeList && timeList.length > 0) {
	current_time = timeList[0].date || '';
}
$(function() {
	
	//隐藏提示信息
	var lrj_time_display_none = getLocalStorage('baojie_choosetime_pop_lrj_time_display_none');
	if(lrj_time_display_none==1){//隐藏提示信息
		$(".pop_lrj_time").hide();
	}else{
		$(".pop_lrj_time").show();
	}
	
	initServiceTime();
	$(".detailtime").on("click", "li:not(.choosed)", function() {
		$(this).addClass("active").siblings().removeClass("active");
		if ($("#confirm").hasClass("onebtn_disable")) {
			$("#confirm").removeClass("onebtn_disable");
		}
	});
	
	$(".weekbar").on("click", "li", function() {
		if (isUpdating) {
			return;
		}
		isUpdating = true;
		// style="color:#7aaff5;"
		$(".timefull").css("color", "#7aaff5");
		$(this).addClass("active").siblings().removeClass("active");
		$(this).children("p").children("span").css("color", "");
		// $("#div").removeAttr("style");
		// getServiceTime(this);
		// var date = $(this).find('p').eq(1).text() || '';
		var current_time = $(".weekbar li.active").eq(0).attr('day');

		var date = new Date(parseFloat(current_time)).Format('yyyy-MM-dd');
		showServiceTime(date);
	});
	$("#confirm")
			.on(
					'click',
					function() {
						if ($(this).hasClass("onebtn_disable"))
							return;
						if (!$(".detailtime li.choosed")) {
							showErrorMsg("请选择服务时间");
						}
						var choosedDay = new Date(parseInt($(
								".weekbar li.active").eq(0).attr('day')))
								.Format('yyyy-MM-dd hh:mm:ss'), choosedTime = $(
								".detailtime li.active").eq(0).find("i").eq(0)
								.text(), baseparameters = $("#baseparameters")
								.val()
								|| '';
						var serviceTime = choosedDay.split(' ')[0] + " " + choosedTime + ":00";
						var showDay =new Date(parseInt($(".weekbar li.active").eq(0).attr('day'))).getDay();
						if (getXingqiFromDayIndex(showDay) != null) {
							setJsonlocals(typelocal, 'showtime', choosedDay
									.split(' ')[0]+ "(周" + getXingqiFromDayIndex(showDay)+ ")" + choosedTime);
						} else {
							setJsonlocals(typelocal, 'showtime', choosedDay
									.split(' ')[0] + " " + choosedTime);
						}
					});
	if (!$("li").hasClass("active")) {
		$(".timefull").css("color", "#7aaff5");
		$(".weekbar").children("li").first().addClass("active").siblings().removeClass("active");
		$(".weekbar").children("li").first().children("p").children("span").css("color", "");
	}
	//懒人节，遮罩层操作
	$(".pop_lrj_time").click(function(){
		$(this).hide();
		setLocalStorage('baojie_choosetime_pop_lrj_time_display_none', 1);
	});
	$(".pop_lrj_time").on("touchmove",function(){
		return false;
	});
});

function getServiceTime(e) {
	var lat = getLocalStorage('lat'), lng = getLocalStorage('lng'), price = getJsonlocals(
			typelocal, 'price'), date = new Date(parseInt($(e).attr('day')))
			.Format('yyyy-MM-dd 00:00:00'), cityid = getCookie('comm_cityid'), $timeBar = $(".detailtime.clf")
			.eq(0);
	$timeBar.empty();
	;
	$(".pull_up_loading").show();
	$.ajax({
		url : "/caboli/servicetime",
		type : "POST",
		data : {
			"lat" : lat,
			"lng" : lng,
			"price" : price,
			"date" : date,
			"cityid" : cityid
		},
		dataType : 'json',
		cache : false,
		async : true,

	});

}

function initServiceDay() {
	var $weekBar = $(".weekbar").eq(0), tmpDate = startDay, liDom = '';
	$weekBar.empty();
	for (var i = 0; i < 7; i++) {
		var tmpstr = tmpDate.Format('MM月dd日');
		if (i === 0) {
			liDom = "<li class='active' day='" + tmpDate.getTime() + "'><p>明天</p><p>" + tmpstr + "</p></li>";
		} else {
			liDom = "<li day='" + tmpDate.getTime() + "'><p>周" + getXingqiFromDayIndex(tmpDate.getDay()) + "</p><p>"+ tmpstr + "</p></li>";
		}
		$weekBar.append(liDom);
		tmpDate = new Date(tmpDate.getTime() + 30 * 60 * 60 * 1000);
	}

}

function initServiceTime(timeList) {
	var $timeBar = $(".detailtime.clf").eq(0), tmpDate = startDay, liDom = '';
	tmpDate.setHours(9, 00, 00);
	for (var i = 0; i < 30; i++) {
		var timeStr = tmpDate.Format('h:mm');
		if ($.inArray(timeStr, timeList) == -1) {
			liDom = '<li class="choosed"><span><i class="from">' + timeStr + '</i></span></li>';
		} else {
			liDom = '<li><span><i class="from">' + timeStr + '</i></span></li>';
		}
		$timeBar.append(liDom);
		tmpDate.setTime(tmpDate.getTime() + (30 * 60 * 1000));
	}
	if (!$("#confirm").hasClass("onebtn_disable")) {
		$("#confirm").addClass("onebtn_disable");
	}
}

function initServiceTime() {
	var $weekBar = $(".weekbar").eq(0), tmpDate = new Date(), liDom = '';
	$weekBar.empty();
	var time_count = -1;
	timeList
			&& $
					.each(
							timeList,
							function(i, obj) {
								tmpDate = new Date(obj.date.replace(/-/g, '/'));
								var isfull = obj.data, full = '';

								if (!isfull || isfull.length == 0) {
									full = '<span class="timefull" style="color:#7aaff5;">[约满]</span>';
									if (time_count == (i - 1)) {
										current_time = new Date(
												new Date(current_time.replace(
														/-/g, "/")).getTime()
														+ 1
														* 24
														* 60
														* 60
														* 1000)
												.Format('yyyy-MM-dd');
									}
									time_count = i;
								}
								var isactive = '';
								if (current_time == obj.date) {
									isactive = 'class="active"';
									setJsonlocals(typelocal, 'active_time',
											current_time);
								}

								// if(i === 0){
								liDom = "<li " + isactive + " day='" + tmpDate.getTime() + "'> <p>" + obj.week + full + "</p><p>" + obj.date + "</p></li>";
								$weekBar.append(liDom);
								servicetimeList[obj.date] = obj.data;
							});
	current_time = getJsonlocals(typelocal, 'active_time') || timeList[0].date;
	showServiceTime(current_time,'init');
}

function showServiceTime(date,state) {
	$(".detailtime.clf").eq(0).empty();
	$(".pull_up_loading").show();

	var $timeBar = $(".detailtime.clf").eq(0), tmpDate = new Date(date.replace(
			/-/g, '/')), liDom = '';
	var curr_time='';
	if (date && $.inArray(date, servicetimeList)) {
		var serviceTimes = servicetimeList[date];
		tmpDate.setHours(9, 00, 00);
		var mrflag = 1;

		for (var i = 0; i < 26; i++) {
			var timeStr = tmpDate.Format('h:mm');
			if ($.inArray(timeStr, serviceTimes) == -1) {
				liDom = '<li class="choosed"><span><i class="from">' + timeStr + '</i></span></li>';
			} else if (mrflag == 1) {
				liDom = '<li class="active"><span><i class="from">' + timeStr + '</i></span></li>';
				mrflag = 2;
				$(".onebtn").removeClass("onebtn_disable");
				curr_time=timeStr;
			} else {
				liDom = '<li><span><i class="from">' + timeStr + '</i></span></li>';
			}
			$timeBar.append(liDom);
			tmpDate.setTime(tmpDate.getTime() + (30 * 60 * 1000));
		}

		$(".pull_up_loading").hide();
	}
	isUpdating = false;
	if(state=='init'){
		paiban_maidian(date,curr_time);
	}
}

//排班埋点
function paiban_maidian(time_date,curr_time,btn){
 try{
	   var uid = getCookie('uid'),
		   lat = getLocalStorage('lat'),
		   lng = getLocalStorage('lng'),
		   type = $('#cateid').val()||'1',
		   cityid = getCookie('comm_cityid'),
		   ostype = getOSType(),
			phone = getCookie('phone'),
			hour = $('#hour').html(),
			comm_pf = $("#comm_pf").val()||'',
			sid = getCookie("sid")||getJsonlocals(typelocal,'sid')||'';
			var m_service_time = '';
			var price = $('#price').text()||'';
			if(timeList){
			   m_service_time = JSON.stringify(timeList);
			}
            var os = 'h5';
			if (ostype== '0') {
            		os= 'Android';
            	} else if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
            		os = 'iPhone OS';
            	} else {
            		os =  'H5';
            	}
			var current_date = time_date;
				var m_param =  '';
			if(btn=='order'){
				m_param = 'action=caboli_service_btn&service_type=jiazheng&comm_pf='+comm_pf+'&user_id='+uid+'&city_id='+cityid+'&mobile='+phone+'&cateid='+type+'&os='+os+'&lat='+lat+'&lon='+lng+'&choose_date='+current_date+'&choose_time='+curr_time+'&hour='+hour+'&price='+price;
			}else{
				m_param = 'action=caboli_service_time_list&service_type=jiazheng&comm_pf='+comm_pf+'&user_id='+uid+'&city_id='+cityid+'&mobile='+phone+'&cateid='+type+'&os='+os+'&lat='+lat+'&lon='+lng+'&choose_date='+current_date+'&choose_time='+curr_time+'&hour='+hour+'&service_time='+m_service_time+'&price='+price;
			}
		   trackClick(m_param);
		}catch (e) {
           console.info(e.name);
           console.info(e.message);
	   }

}

function getNextServiceTime(i) {
	var tmpDate = startDay;
	tmpDate.setTime(tmpDate.getTime() + (30 * 60 * 1000 * i));
	startDay.setHours(8, 00, 00);
	return startDay;
}
function getOSType() {
	if (/(Android)/i.test(navigator.userAgent)) {
		return 0;
	} else if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
		return 1;
	} else {
		return 2;
	}
}
function getXingqiFromDayIndex(num) {
	if (num != 1 && num != 2 && num != 3 && num != 4 && num != 5 && num != 6
			&& num != 0) {
		return null;
	}
	var dict = {
		1 : '一',
		2 : '二',
		3 : '三',
		4 : '四',
		5 : '五',
		6 : '六',
		0 : '日'
	};
	return dict[num];
}
function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}
// 设置值
function setLocalStorage(name, value) {
	window.localStorage.setItem(name, value);
}
// 取值
function getLocalStorage(name) {
	return window.localStorage.getItem(name) || '';
}
// 移除值
function removeLocalStorage(name) {
	window.localStorage.removeItem(name);
}
// 移除所有值
function removeAllLocalStorage() {
	window.localStorage.clear();
}
// 设置setLocalStorage 变量 json
function setJsonlocals(name, key, value) {
	var datas = getLocalStorage(name) || '{}', json = JSON.parse(datas);// eval("("+datas+")");
	json[key] = value;
	console.info(json);
	setLocalStorage(name, JSON.stringify(json));
}
// 根据LocalStorage的key 取出单个值
function getJsonlocals(name, key) {
	var datas = getLocalStorage(name) || '{}', json = JSON.parse(datas);
	return json[key];
}
// 删除json对象单个值
function removeJsonLocal(name, keys) {
	var datas = getLocalStorage(name) || '{}', json = JSON.parse(datas), args = keys
			.split(',');
	for (var i = 0; i < args.length; i++) {
		delete json[args[i]];
	}
	var jsonstr = JSON.stringify(json);
	console.info('值：' + jsonstr);
	setLocalStorage(name, jsonstr);
}
Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}