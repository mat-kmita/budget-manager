import {render} from "react-dom";
import {
    BrowserRouter
} from "react-router-dom";
import App from "./App";
import {Provider} from 'react-redux'
import store from './store'

const rootElement = document.getElementById("root");
render(
    <BrowserRouter>
        <Provider store={store}>
            <App/>
        </Provider>
    </BrowserRouter>,
    rootElement
)
;