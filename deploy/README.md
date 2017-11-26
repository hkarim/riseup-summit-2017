# Environment

## Vagrant

```bash
vagrant up
```
## Ansible

```bash
ansible-playbook --inventory-file=local -v --become --private-key=.vagrant/machines/default/virtualbox/private_key $playbook
```

where `$playbook` is `linux.yml`, `docker.yml` or `db.yml`

## Postgres

```bash
vagrant ssh
docker exec -it postgres bash
postgres -U elmenus elmenus
```

