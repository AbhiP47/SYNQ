console.log("admin user");

document
  .querySelector("#image_file_input")
  .addEventListener("change", function (event) {
    let file = event.target.files[0];

    // ensure a file was actually selected
    if (file) {
        let reader = new FileReader();

        reader.onload = function () {
          const previewImage = document.querySelector("#upload_image_preview");

          // 1. Set the source
          previewImage.setAttribute("src", reader.result);

          // 2. REMOVE the hidden class so the user can actually see it
          previewImage.classList.remove("hidden");
        };

        reader.readAsDataURL(file);
    }
  });