# mustang

mustang is an SSH key distribution service and an SSH-over-WebSocket tunnel.

## Components

- Key generation server
- Local WebSocket proxy
- SSH WebSocket server
- SSH over WebSocket proxy

## Commands

```shell
# Generate a CA key pair
ssh-keygen -f ca -t rsa -b 4096 -f ca -N '' -C CA

# Generate a user key pair
ssh-keygen -f key -t ed25519 -N '' -C raas@roundbox

# Sign a user public key
ssh-keygen -s ca -I raas@roundbox -V -5m:+5m key.pub
```
