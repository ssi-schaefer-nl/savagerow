import axios from 'axios';

class HttpHelper {
  constructor(userService) {
    this.userService = userService;
    this.http = axios.create();
    this.setResponseInterceptor();

  }

  setResponseInterceptor() {
    this.http.interceptors.response.use(
      response => response,
      error => {
        const { status } = error.response;
        if (status === 401) {
          this.userService.logout();
        }
        return Promise.reject(error);
      }
    );
  }

  post(url, data, onSuccess, onFailure) {
    this.http.post(url, data)
      .then(res => { onSuccess(res) })
      .catch(res => { onFailure(res) });
  }

  get(url, onSuccess, onFailure) {

    this.http({ method: 'get', url: url })
      .then(res => { onSuccess(res) })
      .catch(res => { onFailure(res) });
  }


}

export default HttpHelper;