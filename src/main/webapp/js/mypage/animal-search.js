document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.querySelector('.search-input');
    const previewBox = document.getElementById('search-preview-box');
    const previewImg = document.getElementById('preview-img');
    const previewName = document.getElementById('preview-name');

    if (searchInput && previewBox) {
        searchInput.addEventListener('input', function() {
            const keyword = this.value.trim();
            
            if (keyword.length > 0) {
                // JSP에서 설정한 window.contextPath 사용
                const url = window.contextPath + "/animal/animalList.do?action=searchPreview&keyword=" + encodeURIComponent(keyword);

                fetch(url)
                    .then(res => res.text())
                    .then(fileName => {
                        if (fileName && fileName.trim() !== "") {
                            // 이미지 출력 서블릿 호출 경로 (404 방지)
                            previewImg.src = window.contextPath + "/animal/image?fileName=" + fileName;
                            previewName.textContent = keyword;
                            previewBox.style.display = 'block';
                        } else {
                            previewBox.style.display = 'none';
                        }
                    })
                    .catch(err => console.error("통신 실패:", err));
            } else {
                previewBox.style.display = 'none';
            }
        });

        // 외부 클릭 시 닫기
        document.addEventListener('click', (e) => {
            if (!searchInput.contains(e.target) && !previewBox.contains(e.target)) {
                previewBox.style.display = 'none';
            }
        });
    }
});