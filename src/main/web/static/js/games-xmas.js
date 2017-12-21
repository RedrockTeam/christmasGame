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
				enemySpeed: 0.03,
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
				getUser();
				var control = document.createElement("div");
				var father = document.createElement("img");
				$(".over").css("display", "none");
				$(".settings").css("display", "block")
				$(".game-container").removeClass("game-over");
				$(father).attr("src", "../static/images/father.png")
				$(control).attr("class", "control");
				$(control).append(father);
				this.context.container.append(control);
				this.bindSantaClausMove("control");
				this.pointTimer();
				window.enemyTimer = setInterval(function() {
					this.makeEnemy(3, 'left');
					// this.makeEnemy(3, "right");
					this.makeEnemy(3, "top");
				}.bind(this), 4000);

				this.scoreTimer = setInterval(function() {
					if (this.character.point > 15 && this.character.point < 30) {
						this.makeBuff("red");
					}
					if (this.character.point > 30) {
						this.makeBuff("gold");
					}
				}.bind(this), 9000)

				this.hitTimer = setInterval(function() {
					for (var j = 0; j < $(".enemy").length; j++) {
						if (this.gameStatusTrigger($(".control"), $(".enemy")[j])) {
							if (this.character.whosYourDaddy == true) {
								$(".enemy")[j].remove();
							} else {
								this.killGame();
							}
						}
					}

					if (this.gameStatusTrigger($(".control"), $(".gold"))) {
						this.character.whosYourDaddy = true;
						$(".gold").remove();
						this.cancelBuff("gold", 3000);
					}

				}.bind(this), 30);
			}



			// 结束游戏
			this.killGame = function() {
				$(".over").css("display", "block");
				$(".game-container").children().not(".settings").remove();
				$(".settings").css("display", "none");
				$(".game-container").addClass('game-over');
				clearInterval(this.scoreTimer);
				clearInterval(window.enemyTimer)
				clearInterval(this.hitTimer);
				clearInterval(window.timeTimer);
				$(".point").html(this.showPoint(this.character.point));
				postPoint(this.character.point);
				this.pointTimer(1);

			}



			/**
			 * 绑定玩家移动事件
			 * @param  {[type]} xMan [玩家控制角色DOM]
			 */
			this.bindSantaClausMove = function(xMan) {

				var xMan = $("." + xMan);

				var touchstart = function(event) {
					event.preventDefault();
					startClientX = event.touches[0].clientX;
					startClientY = event.touches[0].clientY;
					currentLeft = parseFloat(xMan.css("left"));
					currentTop = parseFloat(xMan.css("bottom"));
				}

				var touchmove = function(event) {
					endClientX = event.touches[0].clientX;
					endClientY = event.touches[0].clientY;
					nowX = currentLeft + endClientX - startClientX;
					nowY = currentTop + startClientY - endClientY;
					maxX = innerWidth - parseInt(xMan.css("width")) - 1;
					maxY = innerHeight - parseInt(xMan.css("height")) - $(".settings").height();

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
					xMan.css("bottom", nowY);
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
				var perWidth = 1.17;
				var perHeight = 1.48;
				// 循环生成敌人
				for (i = 0; i < numbers; i++) {
					var enemy = document.createElement("div");
					$(enemy).attr('class', 'enemy ' + direction);
					// 初始上下左右位置
					switch (direction) {
						case 'left':
							random = parseFloat(_.random(1, 10) * perHeight).toFixed(2);
							// console.log(random);

							$(enemy).css({
								transform: "translate(-1.17rem," + random + "rem)"
							});
							break;
						case 'right':
							random = parseFloat(_.random(1, 10) * perHeight).toFixed(2);
							r = parseFloat(iw.toFixed(2) + 1.17);
							$(enemy).css({
								transform: 'translate(' + r + 'rem,' + random + 'rem)'
							});
							break;
						case 'top':
							random = parseFloat(_.random(1, 7) * perWidth).toFixed(2);
							$(enemy).css({
								transform: 'translate(' + random + 'rem,0.1rem)'
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

				var numberReg = /-?\d+\.?(\d+)?/g;
				window.setTimeout(function() {
					// 产生位移
					switch (direction) {
						case 'left':
							offset = ($(enemy)[0].style.transform.match(numberReg));
							offsetX = parseFloat(offset[0]);
							offsetY = parseFloat(offset[1]);
							offsetX += displacement;
							// console.log(offset);
							$(enemy).css({
								transform: 'translate(' + offsetX + 'rem,' + offsetY + 'rem)'
							});
							if (offsetX > iw) {
								return this.destroyEnemy(enemy);
							}
							break;
						case 'right':
							offset = ($(enemy)[0].style.transform.match(numberReg));
							offsetX = parseFloat(offset[0]);
							offsetY = parseFloat(offset[1]);
							offsetX -= displacement;
							// console.log(offset);
							$(enemy).css({
								transform: 'translate(' + offsetX + 'rem,' + offsetY + 'rem)'
							});
							if (offsetX < -2) {
								return this.destroyEnemy(enemy);
							}
							break;
						case 'top':
							offset = ($(enemy)[0].style.transform.match(numberReg));
							offsetX = parseFloat(offset[0]);
							offsetY = parseFloat(offset[1]);
							offsetY += displacement;
							// console.log(offset)
							$(enemy).css({
								transform: 'translate(' + offsetX + 'rem,' + offsetY + 'rem)'
							});
							if (offsetY > ih) {
								return this.destroyEnemy(enemy);
							}
							break;
						case 'bottom':
							$(enemy).css({
								bottom: parseInt($(enemy).css('bottom')) + displacement + 'px'
							});
							break;
					}
					if (this.gameStatusTrigger($(".control"), $(enemy), true)) {
						$(enemy).addClass('enemy-fast');
						return this.moveEnemy(enemy, 0.08, direction);
					}
					$(enemy).removeClass('enemy-fast');
					return this.moveEnemy(enemy, 0.05, direction);

				}.bind(this), 8)
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
				var perWidth = 0.6;
				// console.log(perWidth);
				$(apple).attr('src', '../static/images/' + type + 'apple.png')
				$(buff).attr('class', 'buff ' + type);
				this.context.container.append(buff);
				buff.append(apple);
				random = _.random(1, 8) * perWidth;
				$(buff).css({
					transform: "translate(" + random + "rem,-1rem)"
				});
				this.moveEnemy(buff, 0.05, "top");
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
					}.bind(this), delayTime)
				}

			}

			/**
			 * 得分计时器
			 * @param  {[type]} init [重置得分，重新计分]
			 */
			this.pointTimer = function(init) {
				var s = 0;
				var ms = 0;

				if (init == 1) {
					s = 0;
					ms = 0;
					this.character.point = 0;
				} else {
					if (window.timeTimer) {
						window.clearInterval(window.timeTimer);
					}
					window.timeTimer = setInterval(function() {
						score = s + "." + ms / 10;
						this.character.point = score;

						ms += 10;
						if (ms == 1000) {
							ms = 0;
							s++;
						}
						$(".seconds").html(this.showPoint(score));
					}.bind(this), 10)

				}

			}


			/**
			 * [showPoint description]
			 * @param  {[type]} score [所得分数]
			 * @return {[type]}       [description]
			 */

			this.showPoint = function(score) {
				var a = score.split(".");
				if (a[0] < 10) {
					scoreBefore = '0' + a[0];
				} else {
					scoreBefore = a[0];
				}
				if (a[1] < 10) {
					scoreAfter = '0' + a[1];
				} else {
					scoreAfter = a[1];
				}
				var time = scoreBefore + '″' + scoreAfter;
				return time;
			}


			/**
			 * 游戏状态触发器
			 * 该触发器触发当前游戏状态，例如是否吃了BUFF，是否碰到了敌人，敌人是否需要加速等等
			 * @param  {[type]}  item [碰撞物体]
			 * @param  {[type]}  hitObj [被撞物体]
			 * @return {[type]} true [触发]
			 */
			this.gameStatusTrigger = function(item, hitObj, accelerate) {
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

				if (accelerate) {
					if (itemFoot + 150 > hitTop && itemRight + 150 > hitLeft && itemTop < hitFoot + 150 && itemLeft < hitRight + 150) {
						return true;
					}
					return false;
				} else {
					if (itemFoot > hitTop && itemRight > hitLeft && itemTop < hitFoot && itemLeft < hitRight) {
						return true;
					}
					return false;
				}
			}

			this.startGame();
		}

	})

})()