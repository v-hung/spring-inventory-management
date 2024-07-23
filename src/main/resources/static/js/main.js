const formActionBtns = document.querySelectorAll(`[formaction]`)
const overlayEl = document.querySelector('.screen-overlay')
const form = document.querySelector('form')

/**
 * @type {HTMLButtonElement | null}
 */
const btnSubmit = form?.querySelector('button[type="submit"]')

/**
 * Start add event
 */

formActionBtns.forEach(v => {
  v.addEventListener("click", handelFormSubmit)
})


const showLoading = () => {
  if (!overlayEl) return

  overlayEl.classList.add('show')
}

/**
 * 
 * @param {Event} e 
 */
function handelFormSubmit (e) {
  if (!form) return

  e.preventDefault()
  
  form.action = e.target.getAttribute("formaction")
  
  if (form.checkValidity()) {
    showLoading()
  }
  
  if (!btnSubmit) {
    form.submit()
  }
  else {
    btnSubmit.click()
  }
}