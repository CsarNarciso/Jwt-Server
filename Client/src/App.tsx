import './App.css'

function App() {

  const baseUrl = 'http://localhost:9090';

  const login = () => {
    fetch(`${baseUrl}/auth/login`, {
      method: 'POST',
      body: JSON.stringify({
        username: "user",
        password: "password"
      })
    });
  }

  const getResource = () => {
    fetch(`${baseUrl}/resource`, {
      credentials: 'include',
      method: 'GET'
    });
  }

  const postResource = () => {
    fetch(`${baseUrl}/resource`, {
      credentials: 'include',
      method: 'POST'
    });
  }

  return (
    <>
      <div>
        <button onClick={login}>Login</button>
        <button onClick={getResource}>GET</button>
        <button onClick={postResource}>POST</button>
      </div>
    </>
  )
}

export default App