console.log("Contacts.js loaded successfully.");
const baseURL = "http://localhost:8081";

let contactModal; // Declare globally so functions can access its instance

document.addEventListener("DOMContentLoaded", () => {
    const viewContactModal = document.getElementById("view_contact_modal");

    if (!viewContactModal) {
        console.error("Error: view_contact_modal element not found in the DOM.");
        return;
    }

    // Options with default animation configurations
    const options = {
        placement: "bottom-right",
        backdrop: "dynamic",
        backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
        closable: true,
        onHide: () => console.log("Modal window is hidden"),
        onShow: () => {
            setTimeout(() => {
                viewContactModal.classList.add("scale-100");
            }, 50);
        },
    };

    const instanceOptions = {
        id: "view_contact_modal",
        override: true,
    };

    contactModal = new Modal(viewContactModal, options, instanceOptions);
});

function openContactModal() {
    if (contactModal) contactModal.show();
}

function closeContactModal() {
    if (contactModal) contactModal.hide();
}

window.loadContactdata = async function(event, id) {
    if (event) {
        event.preventDefault();
        event.stopPropagation();
    }

    console.log("Fetching contact ID:", id);
    try {
        const response = await fetch(`${baseURL}/api/contacts/${id}`);
        if (!response.ok) throw new Error("Network response encountered an error status.");

        const data = await response.json();
        console.log("Retrieved payload:", data);

        document.querySelector("#contact_name").textContent = data.name || "N/A";
        document.querySelector("#contact_email").textContent = data.email || "N/A";
        document.querySelector("#contact_phone").textContent = data.phoneNumber || "N/A";

        const imgElement = document.querySelector("#contact_image");
        imgElement.src = data.picture ? data.picture : '/images/default-user.png';

        document.querySelector("#contact_address").textContent = data.address || "No Address Provided";
        document.querySelector("#contact_about").textContent = data.description || "No Description Added";

        const contactFavorite = document.querySelector("#contact_favorite");
        if (data.favorite) {
            contactFavorite.innerHTML = "<i class='fas fa-heart text-red-500 me-1'></i> Favorite Contact";
        } else {
            contactFavorite.innerHTML = "<span class='text-gray-400 italic text-xs'>Standard Contact</span>";
        }

        const webAnchor = document.querySelector("#contact_website");
        if (data.websiteLink) {
            webAnchor.href = data.websiteLink.startsWith('http') ? data.websiteLink : 'https://' + data.websiteLink;
            webAnchor.textContent = data.websiteLink;
        } else {
            webAnchor.textContent = "Not Available";
            webAnchor.removeAttribute('href');
        }

        const linkedInAnchor = document.querySelector("#contact_linkedIn");
        if (data.linkedIn) {
            linkedInAnchor.href = data.linkedIn.startsWith('http') ? data.linkedIn : 'https://' + data.linkedIn;
            linkedInAnchor.textContent = data.linkedIn;
        } else {
            linkedInAnchor.textContent = "Not Available";
            linkedInAnchor.removeAttribute('href');
        }

        openContactModal();

    } catch (error) {
        console.error("AJAX Processing Error: ", error);
    }
};

window.deleteContact = async function(event, id) {
    // CRITICAL FIX: Stops the browser from instantly navigating to the delete URL
    if (event) {
        event.preventDefault();
        event.stopPropagation(); // Prevents bubbling side effects
    }

    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this deletion operation!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#dc2626",
        cancelButtonColor: "#4b5563",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        // The browser will ONLY navigate to the backend if you click confirm!
        if (result.isConfirmed) {
            console.log("Deletion confirmed. Redirecting to backend for ID:", id);
            window.location.replace(`${baseURL}/user/contacts/delete/` + id);
        }
    });
};