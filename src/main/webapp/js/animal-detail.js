document.addEventListener("DOMContentLoaded", function () {
    const mainImage      = document.getElementById("mainAnimalImage");
    const thumbnailItems = document.querySelectorAll(".thumbnail-item");

    // 썸네일 클릭 시 메인 이미지 교체
    thumbnailItems.forEach(function (item) {
        item.addEventListener("click", function () {
            const img     = item.querySelector(".thumbnail-image");
            const fullSrc = img.dataset.full;

            if (mainImage && fullSrc) {
                mainImage.src = fullSrc;
            }

            thumbnailItems.forEach(function (thumb) {
                thumb.classList.remove("active");
            });

            item.classList.add("active");
        });
    });

    if (thumbnailItems.length > 0) {
        thumbnailItems[0].classList.add("active");
    }
});
