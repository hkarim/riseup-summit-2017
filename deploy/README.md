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

In case you need to drop and recreate the database:

```bash
ansible-playbook --inventory-file=local -v --become -e "dropdb=yes" --private-key=.vagrant/machines/default/virtualbox/private_key db.yml
```

## Postgres

```bash
vagrant ssh
docker exec -it postgres bash
postgres -U elmenus elmenus
```

