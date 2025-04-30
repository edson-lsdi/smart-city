# Attribute Contract

Componente do InterSCity responsável por armazenar os atributos e políticas para o controle de acesso utilizando identidade auto soberanas

## Tecnologias
- Hyperledger fabric 2.5.10
- java v11

## Dependências

- Docker
- hyperledger fabric images
```
docker pull hyperledger/fabric-peer:latest
docker pull hyperledger/fabric-orderer:latest
docker pull hyperledger/fabric-ca
docker pull couchdb:3.3.3
```

### Iniciando os containers e criando o canal
```
    ./network.sh up createChannel -ca -s couchdb
```

### Executando o contrato no canal
```
./network.sh deployCC -ccn smart-city -ccp caminho_ate_o_contrato// -ccl java
```

### Gerando credenciais

```
docker cp ./scripts/createIdentities.sh ca_org1:/
docker exec -it ca_org1 bash /createIdentities.sh
```

### Executando contrato
- Deve especificar qual o contrato a ser executado
```
    attribute:addAttribute
    policy:addPolicy
```