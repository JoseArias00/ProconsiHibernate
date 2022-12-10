package crud.Servicio;

import crud.DAO.ClienteDAO;
import crud.Modelo.ClienteEntity;

import java.util.List;
import java.util.regex.*;

public class UtileríaClienteServicio {

    /**
     * @param cliente Cliente pasado por parámetro para validar sus campos y comprobar que no de errores
     * @throws NullPointerException Ocurre cuando algún campo del cliente o el propio es null
     */
    public static boolean camposValidos(final ClienteEntity cliente) throws NullPointerException {
        if (cliente.equals(null)) {
            throw new NullPointerException("El cliente pasado por parámetro es nulo.");
        } else {
            if (cliente.getDni().equals(null)) {
                throw new NullPointerException("El DNI del cliente es nulo.");
            }
            if (cliente.getNombre().equals(null)) {
                throw new NullPointerException("El nombre del cliente es nulo.");
            }
            if (cliente.getApellidos().equals(null)) {
                throw new NullPointerException("El apellido del cliente es nulo.");
            }
            if (cliente.getFechaAlta().equals(null)) {
                throw new NullPointerException("La fecha de alta del cliente es nulo.");
            }
            if (!((byte) cliente.getTipo() == 0 || (byte) cliente.getTipo() == 1)) {
                throw new NullPointerException("El tipo de cliente es nulo.");
            }
            if ((byte) cliente.getTipo() == 0 && cliente.getCuotaMaxima() == null) {
                throw new NullPointerException("La cuota máxima del cliente de tipo registrado es null");
            }
        }
        return true;
    }

    /**
     * @param identificador El identificador a validar
     * @return Si el identificador pasado por parámetro es válido y esta correctamente formado
     * @throws NullPointerException Ocurre cuando el identificador pasado por parámetros es null
     */
    public static boolean validarIdentificador(final String identificador) throws NullPointerException {
        if (identificador.equals(null)) {
            throw new NullPointerException("El identificador pasado por parámetro es nulo");
        }

        //TODO Cif
        String DNI = "\\d{8}[a-zA-Z]$";
        String NIE = "[x|y|z|X|Y|Z]\\d{7}[A-HJ-NP-TV-Za-hj-np-tv-z]$";

        //TODO comprobar NIE
        if (Pattern.matches(DNI, identificador)) {
            return validarLetraDNI(identificador);
        }

        return false;
    }

    /**
     * @param DNI El DNI que queremos validar
     * @return Si el DNI es válido o no. True si lo es, y false si no
     */
    private static boolean validarLetraDNI(final String DNI) {
        int numerosDNI = Integer.parseInt(DNI.substring(0, 8));
        int resto;
        char miLetra;
        char[] asignacionLetra = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

        resto = numerosDNI % 23;

        miLetra = asignacionLetra[resto];

        return miLetra == Character.toUpperCase(DNI.charAt(8));
    }

    /**
     * @param cliente El cliente que comprobaremos si ya existe otro igual dentro de la base de datos
     * @return True si está repetido, y false si no lo está
     */
    public static boolean comprobarRepetecionCliente(final ClienteEntity cliente) {
        //TODO Cuando se añada la fecha de alta descomentar la igualdad del if
        if (camposValidos(cliente)) {
            ClienteDAO clienteDAO = new ClienteDAO();
            List<ClienteEntity> clientesBaseDatos = clienteDAO.getClientes();

            for (ClienteEntity contadorClientesBD : clientesBaseDatos) {
                if (contadorClientesBD.getDni().equals(cliente.getDni()) &&
                        contadorClientesBD.getNombre().equals(cliente.getNombre()) &&
                        contadorClientesBD.getApellidos().equals(cliente.getApellidos()) &&
                        //contadorClientesBD.getFechaAlta().equals(cliente.getFechaAlta()) &&
                        contadorClientesBD.getTipo() == cliente.getTipo()) {
                    if (cliente.getTipo() == 0 && contadorClientesBD.getTipo() == 0) {
                        if (contadorClientesBD.getCuotaMaxima().equals(cliente.getCuotaMaxima())) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * @param cliente El cliente del que queremos saber si ya existe otro con ese mismo DNI y distintos nombres o apellidos
     * @return True si está repetido, y false si no lo está
     */
    public static boolean dniRepetido(final ClienteEntity cliente) {
        if (camposValidos(cliente)) {
            ClienteDAO clienteDAO = new ClienteDAO();
            List<ClienteEntity> clientesBaseDatos = clienteDAO.getCliente(cliente.getDni());

            for (ClienteEntity contadorClientes : clientesBaseDatos) {
                if (!contadorClientes.getNombre().equals(cliente.getNombre()) || !contadorClientes.getApellidos().equals(cliente.getApellidos())) {
                    return true;
                }
            }

            return false;
        }
    }
}
