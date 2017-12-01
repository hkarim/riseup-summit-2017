# Environment

- Install [Vagrant](https://www.vagrantup.com) for your platform
- Install [Ansible](https://www.ansible.com) for your platform

## Vagrant

To bring up the `Vagrant` virtual-machine, run:
```bash
vagrant up
```

## Ansible

To provision your machine and install `Docker`, `Postres` and `Mongo`, run:

```bash
ansible-playbook --inventory-file=local -v --become --private-key=.vagrant/machines/default/virtualbox/private_key $playbook
```

where `$playbook` is `linux.yml`, `docker.yml` and `db.yml` in that order.

In case you need to drop and recreate the database:

```bash
ansible-playbook --inventory-file=local -v --become -e "dropdb=yes" --private-key=.vagrant/machines/default/virtualbox/private_key db.yml
```

## Postgres

To get a `Postgres` command prompt, run:

```bash
vagrant ssh
docker exec -it postgres bash
postgres -U elmenus elmenus
```

