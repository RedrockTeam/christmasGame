/**
 * 该文件提供所有与游戏相关的方法，并暴露方法接口供外部调用
 * 
 */
(function() {

	$.extend({

		/**
		 * [拓展的名称就叫xmas]
		 * @param  {[type]}   container [游戏的容器]
		 * @param  {Function} callback  [结束游戏后的回调函数, 将回调分数]
		 * @return {[type]}             [description]
		 */
		xMas: function(container, callback) {

			// 游戏运行环境
			this.context = {
				// 敌对
				enemySpeed: 3,
				container: $(container),
				// 普通苹果BUFF，每15秒到30秒掉落一次
				speedBuff: [15, 30],
				// 金苹果BUFF，每30秒到45秒掉落一次
				invincibleBuff: [30, 45],
			}

			// 人物(游戏角色)属性BUFF
			this.character = {
				whosYourDaddy: false,
				speed: 10,
				point: 0
			}

			// 开始游戏
			this.startGame = function() {
				$(".over").css("display", "none");
				$(".game-container").removeClass("game-over");
				var control = document.createElement("div");
				var father = document.createElement("img");
				$(father).attr("src", "../static/images/father.png")
				$(control).attr("class", "control");
				$(control).append(father);
				this.context.container.append(control);
				this.enemyTimer = setInterval(function() {
					this.makeEnemy(2, 'left');
					this.makeEnemy(3, "top");
					this.makeEnemy(2, "right");
				}.bind(this), 5000);
				this.bindSantaClausMove("control");
				this.pointTimer();
				this.scoreTimer = setInterval(function() {
					if (this.character.point > 0 && this.character.point < 30) {
						console.log("yes");
						this.makeBuff("gold");
					}
					if (this.character.point > 30) {
						this.makeBuff("gold")
					}
				}.bind(this), 10000)
				this.hitTimer = setInterval(function() {
					for (var j = 0; j < $(".enemy").length; j++) {
						if (this.gameStatusTrigger($(".control"), $(".enemy")[j])) {
							this.killGame();
						}
					}

					if (this.gameStatusTrigger($(".control"), $(".gold"))) {
						this.character.whosYourDaddy = true;
						$(".gold").remove();
						console.log(this.character.whosYourDaddy);
						this.cancelBuff("gold", 3000);
					}
				}.bind(this), 30);
			}



			// 结束游戏
			this.killGame = function() {
				$(".over").css("display", "block");
				$(".game-container").addClass('game-over')
				$(".game-container").children().remove();
				clearInterval(this.timeTimer);
				clearInterval(this.scoreTimer);
				clearInterval(this.enemyTimer);
				clearInterval(this.hitTimer);
				this.pointTimer(1);
				$(".again").on('click', function() {
					this.startGame();
				}.bind(this));
			}

			/**
			 * 绑定玩家移动事件
			 * @param  {[type]} xMan [玩家控制角色DOM]
			 */
			this.bindSantaClausMove = function(xMan) {

				var xMan = $("." + xMan);

				var touchstart = function(event) {
					startClientX = event.touches[0].clientX;
					startClientY = event.touches[0].clientY;
					currentLeft = parseFloat(xMan.css("left"));
					currentTop = parseFloat(xMan.css("top"));
				}

				var touchmove = function(event) {
					endClientX = event.touches[0].clientX;
					endClientY = event.touches[0].clientY;
					nowX = currentLeft + endClientX - startClientX;
					nowY = currentTop + endClientY - startClientY;
					maxX = innerWidth - parseInt(xMan.css("width")) - 1;
					maxY = innerHeight - parseInt(xMan.css("height"));

					if (nowX < 0) {
						nowX = 0;
					} else if (nowX > maxX) {
						nowX = maxX
					}
					if (nowY < 0) {
						nowY = 0;
					} else if (nowY > maxY) {
						nowY = maxY;
					}

					xMan.css("left", nowX);
					xMan.css("top", nowY);
				}
				xMan[0].addEventListener("touchstart", touchstart);
				xMan[0].addEventListener("touchmove", touchmove);
			}

			/**
			 * 生成敌人
			 * @param  {[type]} numbers   [数量]
			 * @param  {[type]} direction [方向 left, right, top, bottom]
			 */
			this.makeEnemy = function(numbers, direction) {
				// var remSizeCover = parseFloat(document.documentElement.style.fontSize);
				var perWidth = 117;
				var perHeight = 148;


				// 循环生成敌人
				for (i = 0; i < numbers; i++) {
					var enemy = document.createElement("div");
					$(enemy).attr('class', 'enemy ' + direction);
					// 如果是左右的话，那么在Y轴上生成随机位置
					if (_.includes(['left', 'right'], direction)) {
						$(enemy).css({
							top: _.random(6) * perWidth + 'px'
						});
					}
					// 如果是上下的话，那么在X轴上生成随机位置
					if (_.includes(['top', 'bottom'], direction)) {
						$(enemy).css({
							left: _.random(9) * perHeight + 'px'
						});
					}
					// 初始上下左右位置
					switch (direction) {
						case 'left':
							$(enemy).css({
								left: '-117px'
							});
							break;
						case 'right':
							$(enemy).css({
								right: '-117px'
							});
							break;
						case 'top':
							$(enemy).css({
								top: '-148px'
							});
							break;
						case 'bottom':
							$(enemy).css({
								bottom: '-148px'
							});
							break;
					}
					this.context.container.append(enemy);
					// 开始移动
					this.moveEnemy(enemy, this.context.enemySpeed, direction)
				}

			}

			/**
			 * 敌对移动
			 * @param  {[type]} enemy        [敌对DOM]
			 * @param  {[type]} displacement [位移量/速度]
			 * @param  {[type]} direction    [方向]
			 */
			this.moveEnemy = function(enemy, displacement, direction) {
				window.setTimeout(function() {
					// 产生位移
					switch (direction) {
						case 'left':
							$(enemy).css({
								left: parseInt($(enemy).css('left')) + displacement + 'px'
							});
							break;
						case 'right':
							$(enemy).css({
								right: parseInt($(enemy).css('right')) + displacement + 'px'
							});
							break;
						case 'top':
							$(enemy).css({
								top: parseInt($(enemy).css('top')) + displacement + 'px'
							});
							break;
						case 'bottom':
							$(enemy).css({
								bottom: parseInt($(enemy).css('bottom')) + displacement + 'px'
							});
							break;
					}
					$

					// 判断超出屏幕销毁
					if (parseInt($(enemy).css("left")) > innerWidth || parseInt($(enemy).css("right")) > innerWidth || parseInt($(enemy).css("top")) > innerHeight || parseInt($(enemy).css("bottom")) > innerHeight) {
						this.destroyEnemy(enemy);
					}
					// 下一次普通速度
					if (true) {
						return this.moveEnemy(enemy, 2, direction);
					}
					// 下一次判断进入范围加速
					// if (){

					// }

				}.bind(this), 10)
			}

			/**
			 * 超出屏幕外的敌人要将其DOM销毁
			 * @param  {[type]} enemy [敌对DOM元素]
			 * @return {[type]}       [description]
			 */
			this.destroyEnemy = function(enemy) {
				$(enemy).remove();
			}

			/**
			 * BUFF 掉落
			 * @param  {[type]} type [BUFF类型]
			 */
			this.makeBuff = function(type) {
				var buff = document.createElement("div");
				var apple = document.createElement("img");
				$(apple).attr('src', '../static/images/' + type + 'apple.png')
				$(buff).attr('class', 'buff ' + type);
				this.context.container.append(buff);
				buff.append(apple);
				$(buff).css({
					left: _.random(0, innerWidth - 57) + 'px'
				});
				this.moveEnemy(buff, 2, "top");
			}

			/**
			 * 取消获得的BUFF
			 * @param  {[type]} type      [BUFF类型]
			 * @param  {[type]} delayTime [延时时间 S]
			 * @return {[type]}           [description]
			 */
			this.cancelBuff = function(type, delayTime) {
				if (type == "gold") {
					var delay = setTimeout(function() {
						this.character.whosYourDaddy = false;
						console.log(this.character.whosYourDaddy);
					}, delayTime)
				}

			}

			/**
			 * 得分计时器
			 * @param  {[type]} init [重置得分，重新计分]
			 */
			this.pointTimer = function(init) {
				var s = 0;
				var ms = 0;
				this.timeTimer = setInterval(function() {
					score = s + "." + ms / 10;
					this.character.point = score;
					// $(".seconds")[0].innerHTML = this.character.point;
					ms += 10;
					if (ms == 1000) {
						ms = 0;
						s++;
					}
					console.log(this.character.point);
				}.bind(this), 10)

				if (init == 1) {
					s = 0;
					ms = 0;
					this.character.point = 0;
				}

			}

			/**
			 * 游戏状态触发器
			 * 该触发器触发当前游戏状态，例如是否吃了BUFF，是否碰到了敌人，敌人是否需要加速等等
			 * @param  {[type]} status [0: 停止触发，1：继续监听]
			 * @return {[type]} [description]
			 */
			this.gameStatusTrigger = function(item, hitObj) {
				if (item.length == 0 || hitObj.length == 0) {
					return;
				}
				/*检测碰撞元素上下左右的位置*/
				var itemTop = item.offset().top,
					itemFoot = item.offset().top + item.height(),
					itemLeft = item.offset().left,
					itemRight = item.offset().left + item.width();

				/*被碰撞元素的上下左右的位置*/
				var hitTop = $(hitObj).offset().top,
					hitFoot = $(hitObj).offset().top + $(hitObj).height(),
					hitLeft = $(hitObj).offset().left,
					hitRight = $(hitObj).offset().left + $(hitObj).width();
				if (itemFoot > hitTop && itemRight > hitLeft && itemTop < hitFoot && itemLeft < hitRight) {
					return true;
				}
			}
			this.startGame();
		}

	})

})()