import axios from "axios";

const user = JSON.parse(localStorage.getItem("user"));
let headers = {};
if (user && user.token) {
    headers = {
        "Content-type": "application/json",
        "Authorization": "Bearer " + user.token
    }
} else {
    headers = {
        "Content-type": "application/json"
    }
}
export default axios.create({
    baseURL: "http://34.239.112.166:8080",
    headers
});
