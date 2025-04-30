export FABRIC_CA_CLIENT_HOME=/etc/hyperledger/fabric-ca-server/fabric-ca/clients/admin

#Delete old files
rm -fr $FABRIC_CA_CLIENT_HOME/../../../fabric-ca

export PORT=7054
export TLS_CA=$FABRIC_CA_HOME/tls-cert.pem
export ADMIN_URL=https://admin:adminpw@localhost:$PORT
export CERTFILES_PATH=/etc/hyperledger/fabric-ca-server/fabric-ca/clients

#admin
fabric-ca-client enroll -u $ADMIN_URL --tls.certfiles $TLS_CA

sleep 1
#Register
# Create identity

#medico
fabric-ca-client register --id.name maria --id.secret 111 -u $ADMIN_URL --tls.certfiles $TLS_CA --id.attrs 'role=medico:ecert'
fabric-ca-client register --id.name eduarda --id.secret 111 -u $ADMIN_URL --tls.certfiles $TLS_CA --id.attrs 'role=medico;enfermeiro:ecert'

sleep 3

#Enroll
#Generate crypto material

#medico
fabric-ca-client enroll -u https://maria:111@localhost:$PORT --tls.certfiles $TLS_CA --home $CERTFILES_PATH/user1/ 
fabric-ca-client enroll -u https://eduarda:111@localhost:$PORT --tls.certfiles $TLS_CA --home $CERTFILES_PATH/user2/

#Change acess to run locally
chmod -R 777 /etc/hyperledger/fabric-ca-server/fabric-ca


