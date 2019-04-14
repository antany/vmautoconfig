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
### 1. Create user (for this document purpose i used "ftpsuser" as username and "o" as password) </br>
```sudo useradd -m -s /bin/bash ftpsuser```</br>
```sudo passwd ftpsuser```</br>

### 2. Create certificate </br>
```cd /etc && sudo mkdir vsftpd```</br>
```cd vsftpd```</br>
```openssl req -x509 -nodes -days 7300 -newkey rsa:2048 -keyout vsftpd.pem -out vsftpd.pem```
(Note: While giving common name put hostname)</br>
```sudo chmod 600 vsftpd.pem``` </br>

### 3. Setup PAM
```cd /etc/vsftpd ``` <br>
```sudo echo -n "ftpsuser:" | sudo tee .passwd && openssl passwd -1 | sudo tee -a .passwd```
(the password provided here is used by ftp client) <br>

Create file /etc/pam.d/vsftpd and add the below lines <br>
auth required pam_pwdfile.so pwdfile /etc/vsftpd/.passwd <br>
account required pam_permit.so <br>

```echo "auth required pam_pwdfile.so pwdfile /etc/vsftpd/.passwd" | sudo tee /etc/pam.d/vsftpd``` <br>
``` echo "account required pam_permit.so" | sudo tee -a /etc/pam.d/vsftpd``` <br>

### 4. Create userlist file
```sudo echo ftpsuser | sudo tee -a /etc/vsftpd/vsftpd.userlist```

### 5. Create empty ch_root dir
```sudo mkdir -p /var/run/vsftpd/empty```

### 6. modify vsftpd.conf file

```sudo vim /etc/vsftpd.conf ```

and copy the below lines

```
listen=YES
listen_ipv6=NO
anonymous_enable=NO
local_enable=YES
write_enable=YES
local_umask=022
dirmessage_enable=YES
use_localtime=YES
xferlog_enable=YES
connect_from_port_20=YES
chroot_local_user=YES
secure_chroot_dir=/var/run/vsftpd/empty
pam_service_name=vsftpd
pasv_enable=Yes
pasv_min_port=10000
pasv_max_port=11000
user_sub_token=$USER
local_root=/home/$USER
userlist_enable=YES
userlist_file=/etc/vsftpd/vsftpd.userlist
userlist_deny=NO
rsa_cert_file=/etc/vsftpd/vsftpd.pem
rsa_private_key_file=/etc/vsftpd/vsftpd.pem
ssl_enable=YES
allow_anon_ssl=NO
force_local_data_ssl=YES
force_local_logins_ssl=YES
ssl_tlsv1=YES
ssl_sslv2=NO
ssl_sslv3=NO
user_sub_token=$USER
#uncomment below, if the client doesnt support ssl reuse
#require_ssl_reuse=NO 
```

### 7. Test vsftpd
Before enable / disabling service, you can test vsftpd by justing running below

``` sudo vsftpd ```
if no error, try connect using any ftp client, "must use ftp tls explicit connect option"
