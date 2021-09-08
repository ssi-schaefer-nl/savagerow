import axios from 'axios';

class HttpHelper {
  constructor(userService) {
    this.prefix = "/api/v1"
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
    return this.http.post(this.getUrl(url), data)
  }

  get(url) {
    return this.http({ method: 'get', url: this.getUrl(url) })
  }


  put(url, data) {
    return this.http.put(this.getUrl(url), data)
  }

  delete(url) {
    return this.http.delete(this.getUrl(url))
  }


  getUrl(url) {
    return this.prefix+url
  }
}

export default HttpHelper;