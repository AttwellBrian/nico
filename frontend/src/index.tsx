import React from 'react';
import ReactDOM from 'react-dom';
import '@css/css/lib/index.css';

import './index.css';
import App from './App';

const Root = (): JSX.Element => <App />;

ReactDOM.render(<Root />, document.getElementById('root'));
