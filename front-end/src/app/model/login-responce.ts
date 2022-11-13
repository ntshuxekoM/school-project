export class LoginResponce {
	token: string;
	type: string;
	id: number;
	username: string;
	email: string;
	roles: string;
	fullName: string;
	password: string;

	set setToken(value: any) {
		this.token = value;
	}

	set setType(value: any) {
		this.type = value;
	}

	set setId(value: number) {
		this.id = value;
	}

	set setRoles(value: any) {
		this.roles = value;
	}

	set setFullName(value: any) {
		this.fullName = value;
	}

	set setUsername(value: any) {
		this.username = value;
	}

	set setEmail(value: any) {
		this.email = value;
	}

	set setPassword(value: any) {
		this.password = value;
	}


}
