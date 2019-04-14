# Setup FTPS using Vsftpd in Arch linux

## Install required packages
1. vsftpd
2. git (to download aur packages script)
3. libpam_pwdfile (for pam module setup)

### 1. Install VSFTPD
```sudo pacman -S vsftpd```

### 2. Install git
```sudo pacman -S git```

### 3. Install libpam_pwdfile
```git clone https://aur.archlinux.org/libpam_pwdfile.git``` </br>
```cd libpam_pwdfile```</br>
```makepkg -si```</br>
