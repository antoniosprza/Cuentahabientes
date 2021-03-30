package com.springboot.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name= "cuentahabientes ")
public class Cuentahabientes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "cliente")
	private String cliente;
	
	@Column(name = "numero_cuenta")
	private String numeroCuenta;
	
	@Column(name = "usuario")
	private String usuario;
	
	@Column(name = "clave")
	private String clave;
	
	@Column(name = "ultimo_acceso")
	private String ultimoAcceso;
	
	private String mensajeEncriptado;

	public Cuentahabientes() {
		super();
	}

	public Cuentahabientes(long id, String cliente, String numeroCuenta, String usuario, String clave,
			String ultimoAcceso, String mensajeEncriptado) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.numeroCuenta = numeroCuenta;
		this.usuario = usuario;
		this.clave = clave;
		this.ultimoAcceso = ultimoAcceso;
		this.mensajeEncriptado = mensajeEncriptado;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(String ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public String getMensajeEncriptado() {
		return mensajeEncriptado;
	}

	public void setMensajeEncriptado(String mensajeEncriptado) {
		this.mensajeEncriptado = mensajeEncriptado;
	}

	
}
