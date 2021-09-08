import {
  HashRouter
} from "react-router-dom";
import AppLayout from './layout/AppLayout'

const App = () => {
  return (
    <HashRouter>
      <AppLayout/>
    </HashRouter>
  );
}

export default App;
