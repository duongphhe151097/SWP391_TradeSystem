(function($) {
    $.extend($.summernote.lang, {
        'vi-VN': {
            font: {
                bold: 'Chữ đậm',
                italic: 'Chữ nghiêng',
                underline: 'Gạch chân',
                clear: 'Xóa',
                height: 'Chiều cao',
                name: 'Phông chữ',
                superscript: 'Siêu mã',
                subscript: 'Mã phụ',
                strikethrough: 'Gạch ngang',
                size: 'Cỡ chữ',
            },
            image: {
                image: 'Ảnh',
                insert: 'Thêm ảnh',
                resizeFull: '100%',
                resizeHalf: '50%',
                resizeQuarter: '25%',
                resizeNone: 'Không thay đổi',
                floatLeft: 'Căn trái',
                floatRight: 'Căn phải',
                floatNone: 'Bỏ căn',
                shapeRounded: 'Phong cách: Bo góc tròn',
                shapeCircle: 'Phong cách: Hình tròn',
                shapeThumbnail: 'Phong cách: Đóng khung',
                shapeNone: 'Phong cách: Không có',
                dragImageHere: 'Kéo văn bản hoặc ảnh của bạn vào đây',
                dropImage: 'Đặt văn bản hoặc hình ảnh của bạn xuống',
                selectFromFiles: 'Chọn tập tin',
                maximumFileSize: 'Kích thước tệp tối đa',
                maximumFileSizeError: 'Vượt quá kích thước',
                url: 'Url',
                remove: 'Xóa ảnh',
                original: 'Nguyên bản',
            },
            video: {
                video: 'Video',
                videoLink: 'Liên kết video',
                insert: 'Thêm video',
                url: 'Thêm bằng url',
                providers: '(YouTube, Vimeo, Vine, Instagram)',
            },
            link: {
                link: 'Liên kết',
                insert: 'Thêm liên kết',
                unlink: 'Xóa liên kết',
                edit: 'Chỉnh sửa',
                textToDisplay: 'Nội dung hiển thị',
                url: 'Thêm bằng url',
                openInNewWindow: 'Mở trong cửa sổ mới',
            },
            table: {
                table: 'Bảng',
                addRowAbove: 'Thêm hàng vào bên trên',
                addRowBelow: 'Thêm hàng vào bên dưới',
                addColLeft: 'Thêm cột bên trái',
                addColRight: 'Thêm cột bên phải',
                delRow: 'Xóa hàng',
                delCol: 'Xóa cột',
                delTable: 'Xóa bảng',
            },
            hr: {
                insert: 'Chèn dòng phân cách',
            },
            style: {
                style: '스타일',
                p: '본문',
                blockquote: '인용구',
                pre: '코드',
                h1: '제목 1',
                h2: '제목 2',
                h3: '제목 3',
                h4: '제목 4',
                h5: '제목 5',
                h6: '제목 6',
            },
            lists: {
                unordered: '글머리 기호',
                ordered: '번호 매기기',
            },
            options: {
                help: '도움말',
                fullscreen: '전체 화면',
                codeview: '코드 보기',
            },
            paragraph: {
                paragraph: '문단 정렬',
                outdent: '내어쓰기',
                indent: '들여쓰기',
                left: '왼쪽 정렬',
                center: '가운데 정렬',
                right: '오른쪽 정렬',
                justify: '양쪽 정렬',
            },
            color: {
                recent: '마지막으로 사용한 색',
                more: '다른 색 선택',
                background: '배경색',
                foreground: '글자색',
                transparent: '투명',
                setTransparent: '투명으로 설정',
                reset: '취소',
                resetToDefault: '기본값으로 설정',
                cpSelect: '고르다',
            },
            shortcut: {
                shortcuts: '키보드 단축키',
                close: '닫기',
                textFormatting: '글자 스타일 적용',
                action: '기능',
                paragraphFormatting: '문단 스타일 적용',
                documentStyle: '문서 스타일 적용',
                extraKeys: '추가 키',
            },
            help: {
                'insertParagraph': '문단 삽입',
                'undo': '마지막 명령 취소',
                'redo': '마지막 명령 재실행',
                'tab': '탭',
                'untab': '탭 제거',
                'bold': '굵은 글자로 설정',
                'italic': '기울임꼴 글자로 설정',
                'underline': '밑줄 글자로 설정',
                'strikethrough': '취소선 글자로 설정',
                'removeFormat': '서식 삭제',
                'justifyLeft': '왼쪽 정렬하기',
                'justifyCenter': '가운데 정렬하기',
                'justifyRight': '오른쪽 정렬하기',
                'justifyFull': '좌우채움 정렬하기',
                'insertUnorderedList': '글머리 기호 켜고 끄기',
                'insertOrderedList': '번호 매기기 켜고 끄기',
                'outdent': '현재 문단 내어쓰기',
                'indent': '현재 문단 들여쓰기',
                'formatPara': '현재 블록의 포맷을 문단(P)으로 변경',
                'formatH1': '현재 블록의 포맷을 제목1(H1)로 변경',
                'formatH2': '현재 블록의 포맷을 제목2(H2)로 변경',
                'formatH3': '현재 블록의 포맷을 제목3(H3)로 변경',
                'formatH4': '현재 블록의 포맷을 제목4(H4)로 변경',
                'formatH5': '현재 블록의 포맷을 제목5(H5)로 변경',
                'formatH6': '현재 블록의 포맷을 제목6(H6)로 변경',
                'insertHorizontalRule': '구분선 삽입',
                'linkDialog.show': '링크 대화상자 열기',
            },
            history: {
                undo: '실행 취소',
                redo: '재실행',
            },
            specialChar: {
                specialChar: '특수문자',
                select: '특수문자를 선택하세요',
            },
        },
    });
})(jQuery);