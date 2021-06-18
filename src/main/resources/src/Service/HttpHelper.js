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
          // Do something
        }
        return Promise.reject(error);
      }
    );
  }

  post(url, data) {
    return this.http.post(url, data)
  }

  get(url) {
    return this.http({ method: 'get', url: url })
  }


}

export default HttpHelper;