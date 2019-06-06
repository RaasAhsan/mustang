# mustang

```shell
# Generate a CA key pair
ssh-keygen -f ca -t rsa -b 4096 -f ca -N '' -C CA

# Generate a user key pair
ssh-keygen -f key -t ed25519 -N '' -C raas@roundbox

# Sign a user public key
ssh-keygen -s ca -I raas@roundbox -V -5m:+5m key.pub
```