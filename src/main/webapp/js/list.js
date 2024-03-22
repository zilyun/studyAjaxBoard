$(function() {
	function go(page) {
		const limit = $('#viewcount').val();
		/* const data = `limit=${limit}&state=ajax&page=${page}`; */
		const data = { limit: limit, state: "ajax", page: page }
		ajax(data);
	}

	function ajax(sdata) {
		console.log(sdata)

		$.ajax({
			type: "POST",
			data: sdata,
			url: "BoardList.bo",
			dataType: "json",
			cache: false,
			success: function(data) {
				$("#viewcount").val(data.limit);
				$("thead").find("span").text("글 개수 : " + data.listcount);
				console.log(data.listcount +'입니다.')
				
				if (data.listcount > 0) { // 총 갯수가 0보다 큰 경우
					$("tbody").remove();
					let num = data.listcount - (data.page - 1) * data.limit;
					console.log(data.listcount)
					console.log(data.page - 1)
					console.log(data.limit)
					console.log(num)
					let output = "<tbody>";
					$(data.boardlist).each(
						function(index, item) {
							output = '<tr><td>' + (num--) + '</td>'
							const blank_count = item.board_re_lev * 2 + 1;
							let blank = '&nbsp;';
							for (let i = 0; i < blank_count; i++) {
								blank += '&nbsp;&nbsp;';
							}

							let img = "";
							if (item.board_re_lev > 0) {
								img = "<img src='image/line.gif'>";
							}

							let subject = item.board_subject;
							if (subject.length >= 20) {
								subject = subject.substr(0, 20) + "..."; // 0부터 20개의 부분 문자열 추출
							}

							output += "<td><div>" + blank + img
							output += ' <a href="BoardDetail.bo?num=' + item.board_num + '">'
							output += subject.replace(/</g, '&lt;').replace(/>/g, '&gt;')
								+ '</a>[' + item.cnt + ']</div></td>'
							output += '<td><div>' + item.board_name + '</div></td>'
							output += '<td><div>' + item.board_date + '</div></td>'
							output += '<td><div>' + item.board_readcount + '</div></td></tr>'

						})
					output += "</tbody>"
					$('table').append(output) // table 완성
					
					$(".pagination").empty(); // 페이징 처리 영역 내용 제거
					output = "";
					let digit = '이전&nbsp;'
					let href = "";
					if (data.page > 1) {
						href = 'href=javascript:go(' + (data.page - 1) + ')'
					}
					
					//output += setPaging(href, digit);
					
					for (let i = data.startpage; i <= data.endpage; i++) {
						
					}
					
					digit = '&nbsp;다음&nbsp;';
					href="";
					if (data.page < data.maxpage) {
						href = 'href=javascript:go(' + (data.page + 1) + ')';
					}
					
					//output += setPaging(href, digit);
					
					$('.pagination').append(output)
				} // if(data.listcount) > 0 end
			}
		})
	}
	
	$("button").click(function() {
		location.href = "BoardWrite.bo";
	})

	$("#viewcount").change(function() {
		go(1); // 보여줄 페이지를 1페이지로 설정합니다.
	}); // change end

})