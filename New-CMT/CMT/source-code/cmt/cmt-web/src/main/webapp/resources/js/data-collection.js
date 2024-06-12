/**
 * Submit a form after setting a specific action and a method to it.
 *
 * @param action the url
 * @param method Get or Post
 * @param formId the form Id to submit
 */
function navigateTo(action, method, formId) {
  var form = document.getElementById(formId);
  form.action = action;
  form.method = method;
  form.submit();
}


/**
 * Update form action and method
 *
 * @param action the url
 * @param method Get or Post
 * @param formId the form Id to update
 */
function updateForm(action, method, formId) {
  var form = document.getElementById(formId);
  form.action = action;
  form.method = method;
}