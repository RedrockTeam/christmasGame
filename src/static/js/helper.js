// // 工具类
// (function(){
	
// 	/**
// 	 * 初始化设置REM的大小
// 	 */
// 	function initRemSize(){
// 		var docEl = document.documentElement,
//             resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
//             recalc = function() {
//                 var clientWidth = docEl.clientWidth;
//                 if (!clientWidth) return;
//                 if (clientWidth >= 640) {
//                     docEl.style.fontSize = '100px';
//                 } else {
//                     docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
//                 }
//             };

//         if (!document.addEventListener) return;
//         window.addEventListener(resizeEvt, recalc, false);
//         document.addEventListener('DOMContentLoaded', recalc, false);
// 	}


// 	// 导出工具方法
// 	window.HELPER = {
// 		initRemSize: initRemSize
// 	}

// })()