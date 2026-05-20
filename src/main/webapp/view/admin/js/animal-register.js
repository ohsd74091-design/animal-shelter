/* ========================================
   Animal Register JavaScript
   ======================================== */

const mainImageUpload = document.getElementById('mainImageUpload');
const mainImageInput = document.getElementById('mainImageInput');
const additionalImagesInput = document.getElementById('additionalImagesInput');
const imageSlots = document.querySelectorAll('.image-upload-slot');
const personalityTagInput = document.getElementById('personalityTagInput');
const personalityTagContainer = document.getElementById('personalityTagContainer');
const personalityValue = document.getElementById('personalityValue');
const weightInput = document.querySelector('input[name="weight"]');
const sizeDisplay = document.getElementById('sizeDisplay');
const animalRegisterForm = document.getElementById('animalRegisterForm');
const additionalFileMap = new Map();

document.getElementById('currentDate').textContent = new Date().toLocaleDateString('ko-KR');

// ========================================
// 1. 메인 이미지 업로드
// ========================================
mainImageUpload.addEventListener('click', () => mainImageInput.click());

mainImageInput.addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (!file) return;
    if (file.size > 10 * 1024 * 1024) { alert('파일 크기는 10MB를 초과할 수 없습니다.'); return; }
    const reader = new FileReader();
    reader.onload = function(event) {
        mainImageUpload.innerHTML = `
            <img src="${event.target.result}" alt="메인 이미지">
            <button type="button" class="image-remove-btn" onclick="removeMainImage()">
                <span class="material-symbols-outlined">close</span>
            </button>
            <span class="main-badge">Main</span>`;
        mainImageUpload.classList.add('has-image');
    };
    reader.readAsDataURL(file);
});

function removeMainImage() {
    mainImageInput.value = '';
    mainImageUpload.classList.remove('has-image');
    mainImageUpload.innerHTML = `
        <span class="material-symbols-outlined">add_a_photo</span>
        <div class="upload-text">
            <p class="upload-title">메인 사진 업로드</p>
            <p class="upload-subtitle">최대 10MB, JPG/PNG</p>
        </div>
        <span class="main-badge">Main</span>`;
}

// ========================================
// 2. 추가 이미지 업로드
// ========================================
imageSlots.forEach((slot, index) => {
    slot.addEventListener('click', () => {
        additionalImagesInput.click();
        additionalImagesInput.dataset.currentIndex = index;
    });
});

additionalImagesInput.addEventListener('change', function(e) {
    const files = Array.from(e.target.files);
    const currentIndex = parseInt(this.dataset.currentIndex || 0);

    files.forEach((file, i) => {
        const slotIndex = currentIndex + i;
        if (slotIndex >= imageSlots.length) return;
        if (file.size > 10 * 1024 * 1024) { alert('파일 크기는 10MB를 초과할 수 없습니다.'); return; }

        additionalFileMap.set(slotIndex, file);

        const reader = new FileReader();
        reader.onload = function(event) {
            const slot = imageSlots[slotIndex];
            slot.innerHTML = `
                <img src="${event.target.result}" alt="추가 이미지 ${slotIndex + 1}">
                <button type="button" class="image-remove-btn" onclick="removeAdditionalImage(${slotIndex})">
                    <span class="material-symbols-outlined">close</span>
                </button>`;
            slot.classList.add('has-image');
        };
        reader.readAsDataURL(file);
    });
    this.value = '';
});

function removeAdditionalImage(index) {
    additionalFileMap.delete(index);
    const slot = imageSlots[index];
    slot.classList.remove('has-image');
    slot.innerHTML = `<span class="material-symbols-outlined">add</span>`;
    delete slot.dataset.fileIndex;
}

// ========================================
// 3. 성격 해시태그
// ========================================
let personalityTags = [];

personalityTagInput.addEventListener('keypress', function(e) {
    if (e.key === 'Enter' || e.key === ',') { e.preventDefault(); addPersonalityTag(); }
});
personalityTagInput.addEventListener('blur', function() {
    if (this.value.trim()) addPersonalityTag();
});

function addPersonalityTag() {
    const value = personalityTagInput.value.trim();
    if (!value) return;
    if (personalityTags.includes(value)) { alert('이미 추가된 태그입니다.'); personalityTagInput.value = ''; return; }
    if (personalityTags.length >= 3) { alert('성격 태그는 최대 3개까지 추가할 수 있습니다.'); personalityTagInput.value = ''; return; }
    personalityTags.push(value);
    updatePersonalityDisplay();
    personalityTagInput.value = '';
}

function removePersonalityTag(tag) {
    personalityTags = personalityTags.filter(t => t !== tag);
    updatePersonalityDisplay();
}

function updatePersonalityDisplay() {
    personalityTagContainer.innerHTML = personalityTags.map(tag => `
        <span class="personality-tag">
            #${tag}
            <span class="material-symbols-outlined" onclick="removePersonalityTag('${tag}')">close</span>
        </span>`).join('');
    personalityValue.value = personalityTags.join(',');
}

// ========================================
// 4. 크기 자동 분류
// ========================================
if (weightInput) {
    weightInput.addEventListener('input', function() { updateSizeDisplay(parseFloat(this.value)); });
}

function updateSizeDisplay(weight) {
    let sizeText = '미분류', sizeInfo = '체중을 입력해주세요';
    if (weight > 0) {
        if (weight < 10)      { sizeText = '소형'; sizeInfo = '10kg 미만'; }
        else if (weight < 25) { sizeText = '중형'; sizeInfo = '10kg ~ 25kg'; }
        else                  { sizeText = '대형'; sizeInfo = '25kg 이상'; }
    }
    sizeDisplay.innerHTML = `<span class="size-badge">${sizeText}</span><span class="size-info">${sizeInfo}</span>`;
}

// ========================================
// 5. 동물 타입 토글
// ========================================
document.querySelectorAll('input[name="animalType"]').forEach(radio => {
    radio.addEventListener('change', function() {
        document.querySelectorAll('.toggle-btn').forEach(btn => btn.classList.remove('active'));
        this.nextElementSibling.classList.add('active');
    });
});

// ========================================
// 6. 폼 제출
// ========================================
function submitForm() {
    if (!validateForm()) return;

    const formData = new FormData(animalRegisterForm);

    // ✅ mainImage 직접 append (form 밖에 있어서 자동으로 안 담김)
    if (mainImageInput.files.length > 0) {
        formData.set('mainImage', mainImageInput.files[0]);
    }

    // 체크박스 Y/N 처리
    ['vaccination', 'neutered', 'microchip'].forEach(name => {
        const checkbox = document.querySelector(`input[name="${name}"]`);
        formData.set(name, checkbox && checkbox.checked ? 'Y' : 'N');
    });

    // 추가 이미지: FormData 기존값 제거 후 Map에서 직접 append
    formData.delete('additionalImages');
    additionalFileMap.forEach((file) => {
        formData.append('additionalImages', file);
    });

    document.querySelector('.register-container').classList.add('form-loading');

    fetch(ctp + '/admin/animal/register.do', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        document.querySelector('.register-container').classList.remove('form-loading');
        if (data.success) {
            showMessage('동물 등록이 완료되었습니다.', 'success');
            setTimeout(() => { location.href = ctp + '/admin/animal/list.do'; }, 1500);
        } else {
            showMessage('등록 실패: ' + (data.message || '알 수 없는 오류'), 'error');
        }
    })
    .catch(error => {
        document.querySelector('.register-container').classList.remove('form-loading');
        console.error('Error:', error);
        showMessage('등록 중 오류가 발생했습니다.', 'error');
    });
}

// ========================================
// 7. 유효성 검사
// ========================================
function validateForm() {
    if (!mainImageInput.files.length) {
        showMessage('메인 이미지를 업로드해주세요.', 'error');
        mainImageUpload.scrollIntoView({ behavior: 'smooth', block: 'center' });
        return false;
    }
    const requiredFields = [
        { name: 'animalName', label: '동물 이름' },
        { name: 'gender',     label: '성별' },
        { name: 'rescueDate', label: '구조 날짜' }
    ];
    for (const field of requiredFields) {
        const input = document.querySelector(`[name="${field.name}"]`);
        if (!input || !input.value.trim()) {
            showMessage(`${field.label}을(를) 입력해주세요.`, 'error');
            input.focus();
            return false;
        }
    }
    return true;
}

// ========================================
// 8. 임시 저장
// ========================================
function saveDraft() {
    const data = {
        animalName: document.querySelector('[name="animalName"]').value,
        animalType: document.querySelector('[name="animalType"]:checked')?.value,
        breed:      document.querySelector('[name="breed"]').value,
        gender:     document.querySelector('[name="gender"]').value,
        age:        document.querySelector('[name="age"]').value,
        weight:     document.querySelector('[name="weight"]').value,
        personality: personalityValue.value
    };
    localStorage.setItem('animalRegisterDraft', JSON.stringify(data));
    showMessage('임시 저장되었습니다.', 'success');
}

window.addEventListener('DOMContentLoaded', () => {
    const draft = localStorage.getItem('animalRegisterDraft');
    if (draft) {
        const data = JSON.parse(draft);
        if (confirm('임시 저장된 데이터가 있습니다. 불러오시겠습니까?')) {
            Object.keys(data).forEach(key => {
                const input = document.querySelector(`[name="${key}"]`);
                if (input) {
                    if (input.type === 'radio') {
                        const target = document.querySelector(`[name="${key}"][value="${data[key]}"]`);
                        if (target) target.checked = true;
                    } else {
                        input.value = data[key];
                    }
                }
            });
            if (data.personality) {
                personalityTags = data.personality.split(',');
                updatePersonalityDisplay();
            }
        }
    }
});

// ========================================
// 9. 메시지 표시
// ========================================
function showMessage(message, type = 'success') {
    const existingMessage = document.querySelector('.form-message');
    if (existingMessage) existingMessage.remove();
    const icon = type === 'success' ? 'check_circle' : 'error';
    const messageDiv = document.createElement('div');
    messageDiv.className = `form-message ${type}`;
    messageDiv.innerHTML = `<span class="material-symbols-outlined">${icon}</span><span>${message}</span>`;
    const container = document.querySelector('.register-container');
    container.insertBefore(messageDiv, container.firstChild);
    setTimeout(() => messageDiv.remove(), 3000);
}

// ========================================
// 10. 전역 함수 노출
// ========================================
window.submitForm = submitForm;
window.saveDraft = saveDraft;
window.removeMainImage = removeMainImage;
window.removeAdditionalImage = removeAdditionalImage;
window.removePersonalityTag = removePersonalityTag;
