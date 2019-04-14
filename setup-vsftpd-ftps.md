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

## Setup vsftpd
####1. Create user (for this document purpose i used "ftpsuser" as username and "o" as password)
```sudo useradd -m -s /bin/bash ftpsuser```
```sudo passwd ftpsuser```

####2. Create certificate
```cd /etc && sudo mkdir vsftpd```
```cd vsftpd```
```openssl req -x509 -nodes -days 7300 -newkey rsa:2048 -keyout vsftpd.pem -out vsftpd.pem```
(Note: While giving common name put hostname)
```sudo chmod 600 vsftpd.pem```
