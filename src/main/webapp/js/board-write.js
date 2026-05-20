/* ========================================
   Board Write JavaScript
   ======================================== */

// ✅ 커스텀 업로드 어댑터
// CKEditor CDN 버전은 SimpleUploadAdapter가 없어서
// 직접 fetch로 서버에 업로드하는 어댑터를 만들어야 함
class UploadAdapter {
    constructor(loader, uploadUrl) {
        this.loader    = loader;
        this.uploadUrl = uploadUrl;
    }

    upload() {
        return this.loader.file.then(file => new Promise((resolve, reject) => {
            const formData = new FormData();
            // CKEditor가 파트 이름을 "upload"로 전송 → 서버에서 getPart("upload")로 받음
            formData.append('upload', file);

            fetch(this.uploadUrl, {
                method: 'POST',
                body: formData
            })
            .then(res => res.json())
            .then(data => {
                if (data.default) {
                    // 성공: {"default": "이미지URL"} → 에디터 본문에 <img> 자동 삽입
                    resolve({ default: data.default });
                } else if (data.error) {
                    reject(data.error.message);
                } else {
                    reject('업로드 실패');
                }
            })
            .catch(() => reject('서버 오류가 발생했습니다.'));
        }));
    }

    // 업로드 취소 시 호출
    abort() {}
}

// ========================================
// CKEditor 초기화
// ========================================
let editorInstance;

ClassicEditor
    .create(document.querySelector('#content'), {
        toolbar: [
            'heading', '|',
            'bold', 'italic', 'underline', 'strikethrough', '|',
            'bulletedList', 'numberedList', '|',
            'imageUpload', 'link', 'blockQuote', '|',
            'undo', 'redo'
        ],
        placeholder: '내용을 입력해주세요. 이미지는 툴바의 이미지 버튼으로 삽입할 수 있어요.'
    })
    .then(editor => {
        editorInstance = editor;

        // ✅ 커스텀 업로드 어댑터 등록
        // create() 완료 후 등록해야 FileRepository 플러그인이 준비됨
        editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
            return new UploadAdapter(loader, ctp + '/board/imageUpload.do');
        };
    })
    .catch(err => console.error('CKEditor 초기화 오류:', err));

// ========================================
// 폼 제출 시 에디터 내용 동기화
// ========================================
document.getElementById('writeForm').addEventListener('submit', function () {
    // CKEditor의 내용을 textarea에 담아야 서버로 전송됨
    document.getElementById('content').value = editorInstance.getData();
});

// ========================================
// 첨부파일 선택 시 파일명 표시
// ========================================
document.getElementById('uploadFile').addEventListener('change', function () {
    const files = Array.from(this.files).map(f => f.name).join(', ');
    document.getElementById('fileNameDisplay').textContent =
        files || '선택된 파일 없음';
});

